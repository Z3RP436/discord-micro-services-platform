FROM eclipse-temurin:24-jre
WORKDIR /workspace
COPY projects/bot-api/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
