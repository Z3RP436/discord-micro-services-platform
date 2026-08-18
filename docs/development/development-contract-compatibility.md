# Development Contract Compatibility

## Scope

This document defines compatibility rules for RabbitMQ message contracts shared by platform services.

## Contract Versioning Model

- Current schema version: `v1`
- Version marker field: `schemaVersion`
- Message type field: `messageType`
- Every contract message includes:
  - `eventId`
  - `correlationId`
  - `timestamp`

## Current v1 Message Schemas

### `bot.lifecycle.command.v1`

Payload fields:

- `schemaVersion`
- `messageType`
- `eventId`
- `timestamp`
- `correlationId`
- `botId`
- `action`
- `profile`
- `requestedBy`

### `bot.status.event.v1`

Payload fields:

- `schemaVersion`
- `messageType`
- `eventId`
- `correlationId`
- `botId`
- `status`
- `profile`
- `timestamp`

## Compatibility Rules

- Producers MUST emit a supported `schemaVersion`.
- Consumers SHOULD ignore unsupported `schemaVersion` values and log a warning.
- New fields MAY be added in a version as optional/nullable additions only.
- Breaking field changes (remove/rename/type change/semantic change) require a new schema version.
- Queue names are transport channels and are independent from versioning semantics.
- `correlationId` should be propagated from command to resulting events to support tracing.

## Evolution Guidelines

- Introduce `v2` with explicit `messageType` suffix (example: `bot.lifecycle.command.v2`).
- Run dual-read support during migrations when both versions are present.
- Remove old version support only after all producers and consumers are migrated.
