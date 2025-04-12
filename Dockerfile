# Etapa 1 - Build
FROM gradle:8.5.0-jdk17 AS build
WORKDIR /app
COPY api-caderno-lpo/ .

RUN gradle build --no-daemon

# Etapa 2 - Runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 🛑 Substitua pelo nome real do .jar gerado se precisar
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
