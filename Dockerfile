FROM gradle:8.5.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
