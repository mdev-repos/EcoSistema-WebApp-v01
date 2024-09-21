FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/ecosistemas-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_ecosistemas.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_ecosistemas.jar"]