FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./gradlew build -x test

EXPOSE 8080

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "build/libs/app.jar"]
