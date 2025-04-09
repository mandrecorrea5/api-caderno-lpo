# Etapa 1 - Build com Gradle dentro da subpasta
FROM gradle:8.5.0-jdk17 AS build
WORKDIR /app

# Copia só o conteúdo da subpasta api-caderno-lpo
COPY api-caderno-lpo/ .

RUN gradle build --no-daemon --stacktrace --info

# Etapa 2 - Runtime com JAR final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]