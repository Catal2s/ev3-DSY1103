FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

ARG SERVICE_NAME
COPY ${SERVICE_NAME}/pom.xml ./${SERVICE_NAME}/
COPY ${SERVICE_NAME}/src ./${SERVICE_NAME}/src
RUN mvn -f ${SERVICE_NAME}/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG SERVICE_NAME
COPY --from=build /app/${SERVICE_NAME}/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
