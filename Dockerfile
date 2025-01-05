FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
COPY --from=build /app/src/main/resources /app/resources
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]