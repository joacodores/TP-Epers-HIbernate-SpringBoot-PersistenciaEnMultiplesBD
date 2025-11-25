FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copio solo lo necesario para cachear layers
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

# Copio la app
COPY src ./src

# Build sin tests
RUN ./gradlew clean build -x test --no-daemon

# Exponer puerto
EXPOSE 8080

# Ejecutar usando el JAR correcto (NO el plain)
CMD ["sh", "-c", "java -Dspring.profiles.active=prod -jar build/libs/*-SNAPSHOT.jar"]
