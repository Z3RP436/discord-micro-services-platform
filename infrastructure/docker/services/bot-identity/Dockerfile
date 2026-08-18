FROM eclipse-temurin:24-jre
WORKDIR /workspace
COPY projects/bot-identity/build/libs/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]

