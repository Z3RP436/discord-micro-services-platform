# Architecture Implementation Plan

## Goal

Concrete plan from current MVP foundation to a production-ready Discord bot microservices platform.

## Current Phase Position

- Phases 0-5 are implemented as MVP foundation.
- Phase 6 is active (hardening).

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

### Phase 7 - Discord Runtime Integration

Tasks:

- integrate real Discord bot runtime (JDA-based) in bot services
- map lifecycle commands to runtime start/stop behavior
- map command plugins to real Discord interactions

Deliverables:

- bots can connect to Discord and execute real commands

### Phase 8 - Control Plane Expansion

Tasks:

- persist and manage bot-guild assignments
- resource profile policy enforcement (`small`, `medium`, `large`)
- orchestration policy refinement between `bot-api` and `bot-orchestrator`

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
5. Start Discord runtime spike for one bot service.

## Decision Gates (require user approval)

- Discord library integration details for runtime phase.
- Production orchestration target beyond docker compose.
- Security model depth (authn/authz boundary per service).
- Multi-tenant isolation strategy.
- Rollout strategy for future `v2` contracts.

