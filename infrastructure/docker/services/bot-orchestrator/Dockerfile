FROM eclipse-temurin:24-jre
WORKDIR /workspace
COPY projects/bot-orchestrator/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
