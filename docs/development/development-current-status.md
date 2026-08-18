# Development Current Status

## Scope

Status snapshot for the implemented MVP foundation as of 2026-08-18.

## Implemented Architecture Baseline

- Service naming and boundaries are active as approved:
  - `bot-api`
  - `bot-core`
  - `bot-orchestrator`
  - `bot-main`
  - `bot-identity`
  - `bot-logging`
- Shared transport contracts are implemented in `contracts/bot-contracts`.
- Discovery model is implemented as Hybrid:
  - desired bot inventory via GitOps catalog (`infrastructure/bot-catalog/bots.json`)
  - live bot state via RabbitMQ status events
- Docker Compose runtime is implemented in `infrastructure/docker/docker-compose.yml`.
- CI/CD draft workflows exist in `.github/workflows/ci.yml` and `.github/workflows/docker-draft.yml`.
- Confirmed target direction: the platform should support multiple parallel bot instances based on the same service implementation (for example `bot-main`) with distinct stored tokens per instance.

## Currently Available Capabilities

### API and Control

- `bot-api` exposes:
  - `GET /api/bots`
  - `PUT /api/bots/{botId}/guilds/{guildId}/profile`
  - `POST /api/bots/{botId}/lifecycle`
- `bot-orchestrator` exposes:
  - `POST /orchestrator/bots/{botId}/lifecycle`

### Standard Bot

- `bot-main` exposes:
  - `GET /bot/manifest`
  - `POST /bot/plugins/{pluginName}/commands`
- Implemented plugin commands:
  - `moderation`
  - `logging`
  - `config`

### Identity and Permissions

- `bot-identity` exposes:
  - `PUT /identity/guilds/{guildId}/users/{userId}/permissions/{permission}`
  - `GET /identity/guilds/{guildId}/users/{userId}/permissions/{permission}`

### Messaging and Contracts

- RabbitMQ queues in use:
  - `bot.lifecycle.commands`
  - `bot.status.events`
- Contract hardening baseline implemented:
  - schema version field (`schemaVersion`)
  - message type field (`messageType`)
  - metadata (`eventId`, `correlationId`, `timestamp`)
  - compatibility rule in consumers: ignore unsupported schema versions
- Detailed rules are documented in `docs/development/development-contract-compatibility.md`.

## What Is Verified

- `./gradlew test` equivalent was run via Windows wrapper and Java 24 (`.\\gradlew.bat test`) successfully.
- `./gradlew build` equivalent was run via Windows wrapper and Java 24 (`.\\gradlew.bat build`) successfully.

## Current Functional Limits

- No real Discord runtime integration yet (no active JDA lifecycle integration).
- Lifecycle events are wired, but there is no full runtime provisioning/autoscaling engine yet.
- No production-grade retry/DLQ policy yet.
- No full E2E integration test suite for the complete compose stack yet.
- Health/observability baseline is still minimal.

