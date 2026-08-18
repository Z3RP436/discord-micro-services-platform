# Architecture Platform Blueprint

## Scope

This document defines the approved target architecture for the Discord bot microservices platform MVP.

## Confirmed Decisions

- Discovery model: Hybrid GitOps + RabbitMQ live status.
- Service naming: `bot-api`, `bot-core`, `bot-orchestrator`, `bot-template-standard`, `bot-identity`, `bot-logging`.
- Core strategy: Hybrid library + plugin extension points.
- Resource model: static defaults (`small`, `medium`, `large`) with UI overrides per bot and guild.
- Delivery scope: full MVP base with CI workflow drafts.
- Infrastructure first target: Docker Compose + RabbitMQ.
- Persistence: Postgres for configuration/permission data.

## Service Boundaries

### `bot-api`

- Frontend-facing API.
- Reads desired bot inventory from GitOps catalog.
- Tracks live bot status from RabbitMQ events.
- Stores profile overrides per bot/guild.
- Publishes lifecycle commands.

### `bot-orchestrator`

- Receives lifecycle intent (start/stop/restart).
- Publishes standardized lifecycle command messages.
- Keeps orchestration concerns separate from frontend API concerns.

### `bot-template-standard`

- Reference bot service and default implementation.
- Uses `bot-core` plugin interface and reusable primitives.
- Includes moderation, logging, and config plugin commands.
- Reacts to lifecycle commands and emits bot status events.

### `bot-identity`

- Guild/user/permission ownership.
- Persists identity/permission settings in Postgres.

### `bot-logging`

- Consumes and logs status events.
- Defines baseline observability boundary.

### `bot-core`

- Shared library (no runtime service responsibility).
- Declares bot profile model, command context, plugin extension contract, plugin registry.

### `contracts/bot-contracts`

- Shared event/command contracts.
- Versioned transport types used by all services.

## Messaging Topology (RabbitMQ)

- Queue: `bot.lifecycle.commands`
  - Producers: `bot-api`, `bot-orchestrator`
  - Consumers: bot runtime services (starting with `bot-template-standard`)
- Queue: `bot.status.events`
  - Producers: bot runtime services
  - Consumers: `bot-api`, `bot-logging`

## Repository Layout

- `projects/` executable services and shared runtime libraries
- `contracts/` cross-service transport contracts
- `infrastructure/` compose stack, service Dockerfiles, bot catalog
- `docs/` architecture and process documentation
- `memory/` concise durable memory for future agents

## MVP Phase Plan

1. Phase 0: architecture approval and documentation.
2. Phase 1: repo/module reset to target service map.
3. Phase 2: contracts + core plugin API.
4. Phase 3: service skeletons and minimum endpoints.
5. Phase 4: Docker Compose runtime and catalog wiring.
6. Phase 5: CI/CD workflow drafts.
7. Phase 6: iterative hardening (security, observability, tests, production deployment strategy).

## Deferred Topics

- True runtime auto-provisioning from Docker API/Kubernetes APIs.
- Production orchestrator and autoscaling controls.
- Fine-grained ACL model and audit trails.
- Multi-tenant isolation policies.

