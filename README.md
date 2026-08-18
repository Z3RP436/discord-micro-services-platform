# discord-micro-services-platform

MVP foundation for a Discord bot microservices platform with strict service boundaries.

## Services

- `bot-api` frontend-facing API, catalog + live status + profile overrides
- `bot-orchestrator` lifecycle command publisher
- `bot-template-standard` reference bot with moderation/logging/config plugin commands
- `bot-identity` guild/user/permission persistence
- `bot-logging` status event consumer
- `bot-core` shared library and plugin extension contract
- `contracts/bot-contracts` shared transport contracts

## Architecture Inputs

- Discovery: GitOps catalog (`infrastructure/bot-catalog/bots.json`) + RabbitMQ live status
- Profiles: static defaults (`small`, `medium`, `large`) + UI override per bot/guild
- Infra baseline: Docker Compose + RabbitMQ + Postgres

## Local Build

```powershell
.\gradlew.bat build
```

## Run Compose Stack

```powershell
.\gradlew.bat :projects:bot-api:bootJar :projects:bot-orchestrator:bootJar :projects:bot-template-standard:bootJar :projects:bot-identity:bootJar :projects:bot-logging:bootJar
cd infrastructure\docker
docker compose up --build
```

## CI/CD Drafts

- `.github/workflows/ci.yml`
- `.github/workflows/docker-draft.yml`

