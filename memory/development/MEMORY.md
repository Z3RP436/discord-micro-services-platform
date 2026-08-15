# Development Memory

This file contains concise, durable development-related project context.

## Project

- Project name: `discord-micro-services-platform`
- Repository: `https://github.com/Z3RP436/discord-micro-services-platform`
- Local path: `C:\Users\nicop\Documents\Programming\discord-micro-services-platform`

## Java

- Project Java version: 24
- Java toolchain is configured through Gradle.
- Local JDK currently used: OpenJDK 24.0.1

## Gradle

- Build system: Gradle
- Gradle version: 9.6.1
- Gradle Wrapper is committed to the repository.
- Build scripts use Kotlin DSL (`*.gradle.kts`).
- Gradle Version Catalog is used for dependency/version management.
- The Gradle Wrapper should be preferred over a globally installed Gradle version.

## Spring Boot

- Spring Boot version: 4.1.0
- Spring Boot is currently used for backend services.

## Project Structure

Backend services are located under:

`projects/`

Current services:

- `projects/bot-gateway`
- `projects/authorization-service`
- `projects/gamemate-service`
- `projects/platform-api`

The repository also contains:

- `contracts/`
- `frontend/`
- `infrastructure/`
- `docs/`
- `memory/`

## Package Convention

The current Java package namespace is:

`com.z3rp436.discord`

The bot gateway uses:

`com.z3rp436.discord.gateway`

## Verification

The following has been successfully verified:

- Root Gradle build succeeds.
- `:projects:bot-gateway:build` succeeds.
- `:projects:bot-gateway:bootRun` starts successfully.
- Spring Boot starts successfully on Java 24.

## Development Rules

- Follow `AGENTS.md`.
- Keep memory concise and factual.
- Do not store secrets.
- Do not store temporary task state.
- Prefer updating existing entries over duplicating information.