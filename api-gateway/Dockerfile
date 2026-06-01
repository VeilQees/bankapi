FROM maven:3.9.8-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .

COPY common-dto ./common-dto
COPY auth-service ./auth-service
COPY bankapi ./bankapi
COPY notification-service ./notification-service
COPY api-gateway ./api-gateway

RUN mvn clean package -pl api-gateway -am -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/api-gateway/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]