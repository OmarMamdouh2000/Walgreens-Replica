FROM openjdk:21-jdk
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
COPY src/main/resources/google-services.json /app/resources/google-services.json
ENTRYPOINT ["java","-jar","/app.jar"]