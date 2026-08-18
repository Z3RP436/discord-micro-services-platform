# Architecture Phase 6E Token Start Preparation

## Goal

Prepare an implementation-ready plan for the confirmed product flow:

- user enters a Discord bot token in frontend
- user starts the bot
- bot start is blocked when no token exists

## Confirmed Constraints

- Tokens are persisted in database storage.
- Token scope is limited to Discord bot login tokens used by JDA runtime startup.
- `bot-main` is a template/reference service and is not listed as runnable bot in control panel.
- Runtime target is JDA-based Discord bot execution.

## Scope of Phase 6E

- Planning and contract preparation only.
- No direct runtime/JDA feature implementation in this phase.

## Work Packages

### 6E.1 Token Persistence Model

Selected option:

- `6E.1 = 5` (Hybrid: active token store + token history)

Define and approve:

- token entity ownership (bot-level vs bot+guild-level)
- required fields (bot id, token reference, created/updated timestamps, status)
- encryption/masking approach for storage and logs
- rotation/update behavior

Output:

- persistence model draft
- lifecycle rules for token create/update/revoke

#### 6E.1 Data Model Draft (Hybrid)

`bot_token_active` (exactly one active token record per runnable bot)

- `id` (UUID)
- `bot_id` (string, unique, not null)
- `token_cipher` (string, not null)
- `token_fingerprint` (string, not null)
- `token_status` (enum: `ACTIVE`, `REVOKED`, `INVALID`)
- `created_at` (timestamp, not null)
- `updated_at` (timestamp, not null)
- `rotated_at` (timestamp, nullable)
- `rotated_by` (string, nullable)
- `last_validated_at` (timestamp, nullable)
- `last_start_attempt_at` (timestamp, nullable)

`bot_token_history` (append-only token rotation history)

- `id` (UUID)
- `bot_id` (string, not null)
- `token_fingerprint` (string, not null)
- `token_status` (enum: `REPLACED`, `REVOKED`, `INVALID`)
- `archived_at` (timestamp, not null)
- `archived_by` (string, nullable)
- `reason` (string, nullable)

Model constraints:

- `bot_token_active.bot_id` is unique.
- Raw token must never be stored or logged in plain text.
- API responses must never return `token_cipher` or raw token.
- `token_fingerprint` is safe for diagnostics and duplicate checks.

Rotation rules:

- Updating a token writes previous fingerprint/state to `bot_token_history`.
- New token becomes current in `bot_token_active` atomically.
- History is append-only (no updates/deletes except retention policy).

Start-guard data dependency:

- Start is allowed only if `bot_token_active` exists for `bot_id` and `token_status=ACTIVE`.
- Missing active token record or non-active state must reject start request.

### 6E.2 Token Registration API Contract

Define and approve endpoints:

- create or update token
- remove/revoke token
- query token presence status (never return raw token)

Output:

- request/response payload contracts
- validation and error contracts

#### 6E.2 API Draft (for approval)

Selected approvals:

- `6E.2.1 = A`: token format validation strictness is `minimal`.
- `6E.2.2 = A`: `DELETE` uses hard delete semantics for active token records.
- `6E.2.3 = B`: `updatedBy` remains optional in `PUT` payload.

Base path:

- `/api/bots/{botId}/token`

Endpoints:

1. Upsert token

- `PUT /api/bots/{botId}/token`

Request body draft:

```json
{
  "jdaToken": "<discord-bot-token>",
  "updatedBy": "control-panel"
}
```

`updatedBy` is optional metadata.

Response draft:

```json
{
  "botId": "bot-xyz",
  "tokenConfigured": true,
  "tokenStatus": "ACTIVE",
  "tokenFingerprint": "sha256:ab12...",
  "updatedAt": "2026-08-18T12:34:56Z"
}
```

2. Token status query (no secret return)

- `GET /api/bots/{botId}/token`

Response draft:

```json
{
  "botId": "bot-xyz",
  "tokenConfigured": true,
  "tokenStatus": "ACTIVE",
  "tokenFingerprint": "sha256:ab12...",
  "updatedAt": "2026-08-18T12:34:56Z"
}
```

3. Revoke/remove token

- `DELETE /api/bots/{botId}/token`

Delete semantics:

- hard delete active token record for the target bot id
- append delete event metadata into `bot_token_history`

Response draft:

```json
{
  "botId": "bot-xyz",
  "tokenConfigured": false,
  "tokenStatus": "REVOKED",
  "updatedAt": "2026-08-18T12:40:10Z"
}
```

Validation rules draft:

- `jdaToken` required for `PUT` and must not be blank.
- `botId` must exist in runnable bot registry/catalog.
- template/reference bot ids (including `bot-main`) are rejected.
- token value is never returned, logged, or emitted in events.

Error model draft:

- `400 Bad Request`
  - invalid payload
  - blank token
- `404 Not Found`
  - bot id unknown
- `409 Conflict`
  - bot is template/non-runnable
- `422 Unprocessable Entity`
  - token format rejected by validation rules (if enabled)
- `503 Service Unavailable`
  - token persistence temporarily unavailable

Operational semantics draft:

- `PUT` is idempotent for identical token value.
- token update writes prior fingerprint/state to `bot_token_history`.
- `DELETE` hard-deletes the active token record and keeps history entries.
- `GET` only reports presence/status metadata.

### 6E.3 Start Guard Contract

Define and approve:

- start precondition check: token required
- API error behavior when token is missing
- event-level behavior (no lifecycle publish if precondition fails)

Output:

- deterministic start-guard behavior spec
- error codes/messages contract

#### 6E.3 Start-Guard Draft (for approval)

Selected approvals:

- `6E.3.1 = 5`: guard in `bot-api` plus defensive re-check in `bot-orchestrator`.
- `6E.3.2 = 2`: split error codes `BOT_TOKEN_MISSING` and `BOT_TOKEN_INACTIVE`.
- `6E.3.3 = 2`: emit internal audit event `bot.start.blocked`.

Scope:

- Guard applies to lifecycle action `START`.
- Guard does not block `STOP` and `RESTART` by default.
- Guard uses `bot_token_active` as source of truth.

Precondition draft:

- A bot start request is allowed only when:
  - active token record exists for `botId`
  - `token_status = ACTIVE`

Request flow draft:

1. Client calls `POST /api/bots/{botId}/lifecycle` with `action=START`.
2. API validates bot is runnable (not template/reference).
3. API validates token precondition from `bot_token_active`.
4. If valid, API publishes lifecycle command event.
5. If invalid, API rejects request and publishes no lifecycle event.

Error contract draft:

- `412 Precondition Failed`
  - token missing for `botId`
  - token exists but `token_status != ACTIVE`

Error response shape draft:

```json
{
  "errorCode": "BOT_TOKEN_MISSING",
  "message": "Bot start requires an active JDA token.",
  "botId": "bot-xyz",
  "action": "START",
  "timestamp": "2026-08-18T13:00:00Z"
}
```

Event behavior draft:

- On guard pass:
  - publish lifecycle command as usual.
- On guard fail:
  - do not publish lifecycle command.
  - optionally emit audit event (`bot.start.blocked`) without token value.

Observability draft:

- Log fields for blocked starts:
  - `botId`, `action`, `reasonCode`, `correlationId`, `schemaVersion`
- Never log token values or token ciphertext.

Approval options for 6E.3.1 Guard strictness:

1. `START` guarded only (recommended MVP).
2. `START` and `RESTART` guarded.
3. all lifecycle actions guarded (`START`, `STOP`, `RESTART`).
4. guard in `bot-api` only.
5. guard in `bot-api` plus defensive re-check in `bot-orchestrator`.

Approval options for 6E.3.2 Error code strategy:

1. Single code `BOT_TOKEN_REQUIRED` for all guard failures.
2. Split codes: `BOT_TOKEN_MISSING` and `BOT_TOKEN_INACTIVE`.
3. Split codes + include `tokenStatus` in response metadata.
4. return generic `LIFECYCLE_PRECONDITION_FAILED` only.
5. code strategy `2` + internal audit reason mapping (recommended).

Approval options for 6E.3.3 Blocked-start event policy:

1. no additional event on block.
2. emit internal audit event `bot.start.blocked`.
3. emit status event with `status=BLOCKED`.
4. emit both audit + status blocked event.
5. emit audit event only in non-dev environments.

Finalized 6E.3 behavior:

- `START` request is pre-checked in `bot-api`; `bot-orchestrator` applies a defensive re-check before publish.
- Failure response uses:
  - `BOT_TOKEN_MISSING` when no active token record exists
  - `BOT_TOKEN_INACTIVE` when token exists but is not `ACTIVE`
- On guard fail:
  - lifecycle command is not published
  - internal audit event `bot.start.blocked` is published without token data

### 6E.4 Runnable Bot Listing Rule

Define and approve:

- explicit rule to exclude template/reference entries from frontend bot list
- future metadata flag for runnable vs template service

Output:

- listing/filter contract for control panel

#### 6E.4 Listing Contract Draft (for approval)

Selected approvals:

- `6E.4.1 = 3`: hybrid filter source (`serviceType` + temporary static fallback).
- `6E.4.2 = 4`: separate admin-only endpoint later, keep `GET /api/bots` runnable-only.
- `6E.4.3 = 2`: one release grace period with fallback before strict `serviceType` enforcement.

Primary rule draft:

- Control panel bot listing must include only runnable bot entries.
- Template/reference entries (including `bot-main`) must be excluded.

Catalog metadata draft:

- Add field `serviceType` with values:
  - `RUNNABLE_BOT`
  - `TEMPLATE`
  - `INTERNAL`
- Optional compatibility fallback: if field missing, treat known template ids as non-runnable.

API behavior draft (`GET /api/bots`):

- Return only entries where `serviceType=RUNNABLE_BOT`.
- Keep internal/template entries hidden from control-panel responses.
- Preserve existing status/profile fields for returned runnable bots.

Validation draft:

- Start/stop/token endpoints must reject non-runnable ids even if called directly.
- Rejection code for non-runnable target remains `409 Conflict`.

Backward compatibility draft:

- During migration, existing catalog entries without `serviceType` are mapped using fallback rules.
- Once migration is complete, `serviceType` becomes required for new entries.

Approval options for 6E.4.1 Listing filter source:

1. static exclusion list only (quick, brittle)
2. `serviceType` metadata only (clean, requires migration)
3. hybrid: `serviceType` + temporary static fallback (recommended)
4. derive from module naming convention (fragile)
5. derive from runtime registration flags only

Approval options for 6E.4.2 API surface strategy:

1. keep single `GET /api/bots` (runnable only)
2. split into `GET /api/bots` (runnable) + `GET /api/bots/internal` (all)
3. add query flag `includeInternal=true` to existing endpoint
4. provide separate admin-only endpoint later (recommended)
5. keep current endpoint unchanged and filter only in frontend

Approval options for 6E.4.3 Migration enforcement timing:

1. require `serviceType` immediately (strict)
2. one release grace period with fallback (recommended)
3. two release grace periods
4. no hard enforcement, permanent fallback
5. enforce only for newly added bots

Finalized 6E.4 behavior:

- Control-panel listing uses a hybrid filter source during migration:
  - primary: `serviceType=RUNNABLE_BOT`
  - temporary fallback: static exclusion for known template/internal services
- `GET /api/bots` remains runnable-only for frontend usage.
- Internal/template visibility is deferred to a future admin-only endpoint.
- `serviceType` becomes mandatory after one release grace period.

### 6E.5 Migration Path

Define and approve:

- transition from current static catalog-only onboarding
- backward compatibility during migration

Output:

- phased migration sequence for Phase 7/8 implementation

#### 6E.5 Migration Draft (for approval)

Selected approvals:

- `6E.5.1 = 3`: phased rollout with feature flags per step.
- `6E.5.2 = 2`: remove fallback after one release grace period.
- `6E.5.3 = 2`: feature-flag rollback for guard only.

Migration objective:

- move from static catalog-only onboarding to token-based runnable bot lifecycle
- keep existing control-panel functionality available during transition

Phased sequence draft:

1. Contract-first rollout

- introduce token persistence entities and API endpoints behind feature flags
- keep current listing and lifecycle endpoints unchanged externally

2. Dual-read listing rollout

- apply 6E.4 hybrid listing filter (`serviceType` + fallback)
- maintain catalog compatibility for entries without `serviceType`

3. Guard activation rollout

- enable 6E.3 start guard in `bot-api`
- enable defensive re-check in `bot-orchestrator`
- keep stop/restart behavior unchanged per approved 6E.3 scope

4. Token-driven onboarding rollout

- frontend writes JDA token through token API
- start flow depends on active token presence

5. Strict metadata enforcement

- after grace period, require `serviceType` for new and updated catalog entries
- remove temporary fallback exclusions

Rollback draft:

- disable guard feature flag to restore pre-guard start behavior if required
- preserve token data and history on rollback
- keep listing endpoint stable during rollback

Compatibility guarantees draft:

- `GET /api/bots` remains available throughout migration
- lifecycle endpoint path remains unchanged
- non-runnable/template exclusion continues to apply
- rollout is performed per service boundary; no global application shutdown is required
- updated services must remain backward compatible with still-running older services during rollout window
- messaging contracts stay additive/compatible for mixed-version operation during service-by-service deployment

Approval options for 6E.5.1 rollout strategy:

1. big-bang switch in one release
2. two-step rollout (contracts + guard together)
3. phased rollout with feature flags per step (recommended)
4. frontend-first rollout
5. backend-only rollout then frontend catch-up

Approval options for 6E.5.2 fallback retirement:

1. remove fallback immediately after first successful release
2. remove fallback after one release grace period (recommended)
3. remove fallback after two release grace periods
4. keep fallback permanently
5. remove fallback only when all bots are re-registered via token flow

Approval options for 6E.5.3 rollback policy:

1. no rollback support beyond code revert
2. feature-flag rollback for guard only (recommended)
3. feature-flag rollback for guard and token API exposure
4. full dual-mode rollback for all migration steps
5. rollback disabled in production, manual intervention only

Finalized 6E.5 behavior:

- Migration uses phased feature-flag rollout per microservice.
- Services are updated independently (rolling/service-by-service), without requiring full platform downtime.
- Backward-compatible contracts are mandatory during mixed-version windows.
- Listing fallback is removed after one release grace period.
- Start-guard rollback is available through feature flags if operational issues occur.

## Exit Criteria

Phase 6E is complete when:

- token persistence model is approved
- token API contracts are approved
- start guard behavior is approved
- runnable bot listing rule is approved
- migration path is approved

## Next Phase Link

After 6E approval, implement in:

- Phase 7 (runtime/JDA integration)
- Phase 8 (control-plane token registration and assignment UX)











