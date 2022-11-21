FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} fileReader-app.jar
ENTRYPOINT ["java","-jar","/fileReader-app.jar"]