# Architecture Memory

This file contains concise, durable architecture-related project context.

## Confirmed Decisions

- Target model: Discord bot microservices platform with strict service boundaries.
- Discovery model: Hybrid GitOps catalog + RabbitMQ live status.
- Service names: `bot-api`, `bot-core`, `bot-orchestrator`, `bot-template-standard`, `bot-identity`, `bot-logging`.
- Core strategy: Hybrid library + plugin extension points.
- Bot evolution principle: bot-specific features should be implementable without changing core behavior.
- Infrastructure progression: Docker Compose first, production orchestration later.
- Contract versioning baseline: schema-tagged RabbitMQ messages with `schemaVersion=v1` and typed `messageType`.
- Compatibility baseline: consumers ignore unsupported schema versions and log warnings.
- Implementation roadmap is documented in `docs/architecture/architecture-implementation-plan.md`.

## Rules

- Keep entries concise.
- Store only confirmed project knowledge.
- Do not store unapproved decisions or speculative ideas.
- Do not store secrets or temporary task state.
- Prefer updating existing entries over creating duplicates.
