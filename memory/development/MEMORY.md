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
- Preferred local JDK path for commands: `C:\Users\nicop\.jdks\openjdk-24.0.1`

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

- `projects/bot-api`
- `projects/bot-orchestrator`
- `projects/bot-template-standard`
- `projects/bot-identity`
- `projects/bot-logging`

Shared runtime library:

- `projects/bot-core`

Shared transport contracts:

- `contracts/bot-contracts`

The repository also contains:

- `contracts/`
- `frontend/`
- `infrastructure/`
- `docs/`
- `memory/`

## Package Convention

The current Java package namespace is:

`com.z3rp436.discord`

New services follow:

`com.z3rp436.discord.bot.*`

Core follows:

`com.z3rp436.discord.core`

## Verification

The following has been successfully verified:

- Root project uses Java 24, Gradle Wrapper, Kotlin DSL, and version catalogs.
- Previous `bot-gateway` draft context should not be treated as target architecture.

## Development Rules

- Follow `AGENTS.md`.
- Keep memory concise and factual.
- Do not store secrets.
- Do not store temporary task state.
- Prefer updating existing entries over duplicating information.
- Contract compatibility rules are documented in `docs/development/development-contract-compatibility.md`.
- Current implementation status is documented in `docs/development/development-current-status.md`.
- New bot setup guide is documented in `docs/development/development-new-bot-onboarding.md`.


