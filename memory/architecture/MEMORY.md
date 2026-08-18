# Architecture Memory

This file contains concise, durable architecture-related project context.

## Confirmed Decisions

- Target model: Discord bot microservices platform with strict service boundaries.
- Discovery model: Hybrid GitOps catalog + RabbitMQ live status.
- Service names: `bot-api`, `bot-core`, `bot-orchestrator`, `bot-main`, `bot-identity`, `bot-logging`.
- Core strategy: Hybrid library + plugin extension points.
- Bot evolution principle: bot-specific features should be implementable without changing core behavior.
- Infrastructure progression: Docker Compose first, production orchestration later.
- Contract versioning baseline: schema-tagged RabbitMQ messages with `schemaVersion=v1` and typed `messageType`.
- Compatibility baseline: consumers ignore unsupported schema versions and log warnings.
- Implementation roadmap is documented in `docs/architecture/architecture-implementation-plan.md`.
- Bot registration direction: Discord bot instances are managed through token-based registration (JDA runtime target).
- Token policy: bot tokens must be stored in a database-backed persistence model.
- Token scope: only Discord JDA login tokens are in scope for this flow.
- Start policy: a bot must not be started when no token is stored for that bot.
- UI visibility rule: `bot-main` is a runnable main service and should be listed in the control panel.
- Template policy: template-service concept is dropped; only real bot services are listed.
- Bot-main policy: `bot-main` is a real runnable bot service and also requires a JDA token.
- Start-guard reason codes: `TOKEN_MISSING`, `TOKEN_INVALID`, `BOT_NOT_RUNNABLE`.
- Contract policy: OpenAPI specs are source of truth and service interfaces must follow v1 contracts.
- Scaling target: the platform should support many Discord bot instances based on the same bot service implementation (for example `bot-main`), with separate stored tokens per instance so they appear as distinct Discord bots.
- Phase 6E focus: planning package for token registration + start-guard + runnable-listing rules before runtime implementation.
- Phase 6E reference: `docs/architecture/architecture-phase-6e-token-start-preparation.md`.
- Phase 6E.1 selected option: `5` (hybrid token persistence with active token table + append-only token history table).
- Phase 6E.2 finalized approvals: minimal token validation, hard-delete active token on `DELETE`, and optional `updatedBy` on `PUT`.
- Phase 6E.3 finalized approvals: guard in bot-api plus defensive re-check in bot-orchestrator; blocked-start emits internal audit event `bot.start.blocked`.
- Phase 6E.4 finalized approvals: listing returns real bot services with `configured`/`runnable`/`reasonCode` to guide UI behavior.
- Phase 6E.5 finalized approvals: phased feature-flag rollout, one release grace period before fallback removal, and guard-only feature-flag rollback.
- Migration rule: service-by-service microservice updates without whole-platform downtime; mixed-version compatibility is required during rollout windows.

## Rules

- Keep entries concise.
- Store only confirmed project knowledge.
- Do not store unapproved decisions or speculative ideas.
- Do not store secrets or temporary task state.
- Prefer updating existing entries over creating duplicates.
