# Etapa 1: compilar el proyecto con Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY user_crud_service/pom.xml .
RUN mvn dependency:go-offline -B
COPY user_crud_service/src ./src
RUN mvn package -DskipTests -B

# Etapa 2: imagen final solo con el JAR
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/api-rest-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
