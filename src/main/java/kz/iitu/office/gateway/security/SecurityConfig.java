package kz.iitu.office.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
           .antMatchers("/v2/api-docs",
                   "/configuration/ui",
                   "/swagger-resources/**",
                   "/configuration/security",
                   "/swagger-ui.html",
                   "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .ignoringAntMatchers("/actuator/hystrix.stream")
            .disable()
            .authorizeRequests()
            .antMatchers("/auth/**")
            .not()
            .fullyAuthenticated()
            .antMatchers("/api/**")
            .permitAll()
            .antMatchers("/reserves/**")
            .permitAll()
            .antMatchers("/api/admin/**")
            .hasAuthority("ADMIN")
            .anyRequest()
            .authenticated()
            .and()
            .logout()
            .permitAll()
            .and()
            .httpBasic()
            .and()
            .addFilterAfter(
                    new JwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .cors()
            .configurationSource(corsConfigurationSource())
        ;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT","PATCH"));
        source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
        return source;
    }
}
