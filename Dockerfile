FROM openjdk:8

ADD /target/office-gateway-0.0.1-SNAPSHOT.jar office-gateway-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "office-gateway-0.0.1-SNAPSHOT.jar"]

EXPOSE 8762