plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    java
}

val composeWorkingDir = layout.projectDirectory.dir("infrastructure/docker").asFile

val dockerPrepareJars = tasks.register("dockerPrepareJars") {
    dependsOn(
        ":projects:bot-api:bootJar",
        ":projects:bot-orchestrator:bootJar",
        ":projects:bot-template-standard:bootJar",
        ":projects:bot-identity:bootJar",
        ":projects:bot-logging:bootJar"
    )
}

val dockerComposeUpNoTests = tasks.register<Exec>("dockerComposeUpNoTests") {
    group = "docker"
    description = "Builds required jars and starts the docker compose stack."
    dependsOn(dockerPrepareJars)
    workingDir = composeWorkingDir
    commandLine("docker", "compose", "up", "-d", "--build")
}

val dockerComposeUpWithTests = tasks.register("dockerComposeUpWithTests") {
    group = "docker"
    description = "Runs tests and starts the docker compose stack."
    dependsOn("test")
    dependsOn(dockerComposeUpNoTests)
}
dockerComposeUpNoTests.configure {
    mustRunAfter("test")
}

val dockerComposeDebugUpNoTests = tasks.register<Exec>("dockerComposeDebugUpNoTests") {
    group = "docker"
    description = "Builds required jars and starts the docker compose debug stack."
    dependsOn(dockerPrepareJars)
    workingDir = composeWorkingDir
    commandLine(
        "docker", "compose",
        "-f", "docker-compose.yml",
        "-f", "docker-compose.debug.yml",
        "up", "-d", "--build"
    )
}

val dockerComposeDebugUpWithTests = tasks.register("dockerComposeDebugUpWithTests") {
    group = "docker"
    description = "Runs tests and starts the docker compose debug stack."
    dependsOn("test")
    dependsOn(dockerComposeDebugUpNoTests)
}
dockerComposeDebugUpNoTests.configure {
    mustRunAfter("test")
}

tasks.register<Exec>("dockerComposeDown") {
    group = "docker"
    description = "Stops and removes the docker compose stack with volumes."
    workingDir = composeWorkingDir
    commandLine("docker", "compose", "down", "-v")
}

tasks.register<Exec>("dockerComposeDebugDown") {
    group = "docker"
    description = "Stops and removes the docker compose debug stack with volumes."
    workingDir = composeWorkingDir
    commandLine(
        "docker", "compose",
        "-f", "docker-compose.yml",
        "-f", "docker-compose.debug.yml",
        "down", "-v"
    )
}

group = "com.z3rp436"
version = "0.1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(24)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}