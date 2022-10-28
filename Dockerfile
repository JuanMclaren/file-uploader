FROM openjdk:8

COPY target/fileReader-app.jar fileReader-app.jar

ENTRYPOINT ["java ,"-jar" ,"/fileReader-app.jar"]