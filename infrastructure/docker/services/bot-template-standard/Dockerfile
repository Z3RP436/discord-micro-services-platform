FROM eclipse-temurin:24-jre
WORKDIR /workspace
COPY projects/bot-template-standard/build/libs/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
