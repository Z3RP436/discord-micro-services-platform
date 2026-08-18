FROM eclipse-temurin:24-jre
WORKDIR /workspace
COPY projects/bot-logging/build/libs/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]

