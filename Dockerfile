FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia tu jar. Usa wildcard para que no importe el nombre exacto.
COPY build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
