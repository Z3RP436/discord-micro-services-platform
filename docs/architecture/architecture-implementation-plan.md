# Architecture Implementation Plan

## Goal

Concrete plan from current MVP foundation to a production-ready Discord bot microservices platform.

## Current Phase Position

- Phases 0-5 are implemented as MVP foundation.
- Phase 6 is active (hardening).

## Newly Confirmed Product Decisions (2026-08-18)

- Bot onboarding target flow: provide token in frontend, then start bot.
- Token persistence: store bot tokens in database persistence (not file-based).
- Runtime guard: block bot start requests if no token is present.
- Listing visibility: `bot-main` is a runnable main service; only non-runnable internal services are excluded from control-panel listings.
- Phase 6E.1 selected model: Option 5 hybrid token persistence (`bot_token_active` + append-only `bot_token_history`).
- Phase 6E.3 selected guard model: dual-check (`bot-api` + defensive `bot-orchestrator`) with split guard errors (`BOT_TOKEN_MISSING`, `BOT_TOKEN_INACTIVE`) and blocked-start audit event emission.

## Work Plan

### Phase 6A - Contract and Runtime Hardening (in progress)

Status:

- Implemented: schema versioning `v1`, message metadata, consumer compatibility checks.
- Remaining:
  - add explicit negative tests for unsupported schema versions in consumers
  - add error handling policy for malformed payloads

Deliverables:

- stable contract evolution baseline
- consumer behavior for incompatible messages is deterministic

### Phase 6B - E2E Platform Verification

Tasks:

- add compose-based smoke flow script:
  - start stack
  - call lifecycle endpoint
  - validate status event roundtrip
- add integration tests for major API flows

Deliverables:

- repeatable local verification script
- first E2E integration test suite

### Phase 6C - Security Baseline

Tasks:

- validate and sanitize incoming API payloads consistently
- central exception mapping and safe error responses
- externalize sensitive env defaults for compose/dev

Deliverables:

- minimum API hardening for dev/staging
- documented secret handling baseline

### Phase 6D - Observability Baseline

Tasks:

- add health endpoints strategy for each service
- unify structured log fields (`eventId`, `correlationId`, `botId`, `schemaVersion`)
- define minimal metrics list

Deliverables:

- consistent diagnostics across services
- operational readiness for debugging incidents

### Phase 6E - Token-Start Flow Preparation (next)

Tasks:

- define persistence model for bot tokens in database storage
- define control-plane start guard contract (reject start when token missing)
- define catalog/listing rule so only runnable services are returned to control panel
- define API contract for token registration/update/delete workflows
- define migration path from current static catalog flow to token-based onboarding

Deliverables:

- approved token persistence model (data fields + ownership + lifecycle)
- approved API contract draft for token registration workflows
- approved start-guard behavior definition and error model
- approved UI listing rule for runnable-only filtering
- implementation-ready Phase 7 input package

### Phase 7 - Discord Runtime Integration

Tasks:

- integrate real Discord bot runtime (JDA-based) in bot services
- map lifecycle commands to runtime start/stop behavior
- map command plugins to real Discord interactions
- define token usage contract between persistence and runtime startup

Deliverables:

- bots can connect to Discord and execute real commands

### Phase 8 - Control Plane Expansion

Tasks:

- persist and manage bot-guild assignments
- resource profile policy enforcement (`small`, `medium`, `large`)
- orchestration policy refinement between `bot-api` and `bot-orchestrator`
- add bot token registration API + frontend flow
- enforce start guard in API/orchestrator path (reject start when token missing)
- hide non-runnable/internal services from runnable bot list returned to frontend

Deliverables:

- frontend can fully control assignment and lifecycle intent

### Phase 9 - Deployment Maturity

Tasks:

- upgrade CI workflows from draft to gated pipelines
- define image tagging/release strategy
- introduce staging deployment workflow

Deliverables:

- reliable build-test-release flow
- repeatable deployment path

## Priority Backlog (Short Term)

1. Add E2E smoke script for compose flow.
2. Add unsupported-version consumer tests.
3. Add centralized API error handling.
4. Define health endpoints and standard log schema.
5. Complete Phase 6E token-start preparation package.
6. Start Discord runtime spike for one bot service.

## Decision Gates (require user approval)

- Discord library integration details for runtime phase.
- Production orchestration target beyond docker compose.
- Security model depth (authn/authz boundary per service).
- Multi-tenant isolation strategy.
- Rollout strategy for future `v2` contracts.

