FROM openjdk:11-jre-slim-stretch

ADD target/joke-0.0.1-SNAPSHOT.jar joke-application

ENTRYPOINT ["java", "-jar", "joke-application"]
