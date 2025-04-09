# Usa uma imagem Java 17 oficial
FROM eclipse-temurin:17-jdk

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o .jar gerado para dentro do container
COPY api-caderno-lpo/build/libs/*.jar app.jar

# Define o comando que será executado quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]
