# Development New Bot Onboarding

## Goal

This guide explains how to add a new bot service so it can be discovered, started/stopped, and used in the platform.

## Prerequisites

- Java 24 local path for commands: `C:\Users\nicop\.jdks\openjdk-24.0.1`
- Gradle Wrapper (`.\\gradlew.bat`)
- Docker + Docker Compose
- RabbitMQ + Postgres are started through compose

## High-Level Flow

1. Create a new service module under `projects/`.
2. Register module in `settings.gradle.kts`.
3. Implement bot manifest + lifecycle listener + plugin commands.
4. Add Dockerfile for the service.
5. Add service to `infrastructure/docker/docker-compose.yml`.
6. Add bot entry to `infrastructure/bot-catalog/bots.json`.
7. Build and test.
8. Smoke test via API.

## Step-by-Step

### 1) Create module structure

Create a new folder, example: `projects/bot-my-feature`.

Add:

- `build.gradle.kts`
- `src/main/java/.../BotMyFeatureApplication.java`
- `src/main/resources/application.yml`

Recommended dependencies:

- `implementation(project(":projects:bot-core"))`
- `implementation(project(":contracts:bot-contracts"))`
- `implementation(libs.spring.boot.starter)`
- `implementation(libs.spring.boot.starter.web)`
- `implementation(libs.spring.boot.starter.amqp)`
- `implementation(libs.spring.boot.starter.validation)`

### 2) Register module in Gradle settings

Add the module in `settings.gradle.kts` include list:

- `":projects:bot-my-feature"`

### 3) Implement required bot runtime contracts

Your bot should provide:

- `GET /bot/manifest`
  - return bot id, display name, capabilities, default profile
- Rabbit listener for `bot.lifecycle.commands`
  - consume `BotLifecycleCommand`
  - ignore unsupported `schemaVersion`
  - ignore commands for other bot ids
- status event publishing to `bot.status.events`
  - send `BotStatusEvent.v1(...)`
  - propagate incoming `correlationId`

### 4) Implement plugin commands

- Implement `BotPlugin` from `bot-core` for each capability.
- Register plugins through the service plugin registry.
- Expose command endpoint:
  - `POST /bot/plugins/{pluginName}/commands`

### 5) Add Docker image build file

Create:

- `infrastructure/docker/services/bot-my-feature/Dockerfile`

Use the same runtime pattern as existing services:

- copy built jar from `projects/bot-my-feature/build/libs/*.jar`
- expose service port

### 6) Add service to compose

In `infrastructure/docker/docker-compose.yml`, add:

- build context + Dockerfile path
- `RABBITMQ_HOST` env
- `BOT_ID`, `BOT_DISPLAY_NAME`, `BOT_PROFILE`
- port mapping
- dependency on `rabbitmq`

### 7) Add bot to GitOps catalog

Edit `infrastructure/bot-catalog/bots.json` and append a bot entry:

- `botId`
- `displayName`
- `defaultProfile`
- `capabilities`

Without this step, `bot-api` will not list the bot in desired inventory.

### 8) Verify locally

Use Java 24 env for the shell and run build/tests:

```powershell
Set-Location "C:\Users\nicop\Documents\Programming\discord-micro-services-platform"
$env:JAVA_HOME="C:\Users\nicop\.jdks\openjdk-24.0.1"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :projects:bot-my-feature:test
.\gradlew.bat :projects:bot-my-feature:build
```

Start compose stack:

```powershell
Set-Location "C:\Users\nicop\Documents\Programming\discord-micro-services-platform\infrastructure\docker"
docker compose up --build
```

### 9) Smoke test usage

Check bot catalog visibility:

```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/bots" -Method Get
```

Trigger lifecycle command:

```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/bots/bot-my-feature/lifecycle" -Method Post -ContentType "application/json" -Body '{"action":"START","profile":"MEDIUM","requestedBy":"local-dev"}'
```

Execute plugin command:

```powershell
Invoke-RestMethod -Uri "http://localhost:8083/bot/plugins/moderation/commands" -Method Post -ContentType "application/json" -Body '{"guildId":"g1","userId":"u1","command":"ban","arguments":{}}'
```

## Definition of Done for a New Bot

- Module is included in `settings.gradle.kts`.
- Bot appears in `GET /api/bots`.
- Bot receives lifecycle commands and emits status events.
- At least one plugin command is callable.
- Service builds and tests pass.
- Service can run from docker compose.

