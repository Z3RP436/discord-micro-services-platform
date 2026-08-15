# 03 – Services, Events and Commands

## 1. Service definition

A service is an independently deployable application with:

- unique service ID
- version
- configuration schema
- resource profile
- subscribed event types
- command definitions
- health endpoint
- metrics
- logs
- optional persistent database

Example:

```yaml
service:
  id: gamemate
  name: GameMate
  version: 1.0.0

discord:
  events:
    - discord.member.joined
    - discord.member.left
    - discord.interaction.command

  commands:
    - event
    - event-create
    - event-list
    - event-cancel
```

## 2. Event flow

```text
Discord
  ↓
JDA
  ↓
Bot Gateway
  ↓
PlatformEvent
  ↓
RabbitMQ exchange
  ├── GameMate queue
  ├── Moderation queue
  └── Statistics queue
```

RabbitMQ exchange/queue topology should be declarative.

Suggested model:

```text
exchange: platform.events
routing key: discord.member.joined

queue: gamemate.events
queue: moderation.events
queue: statistics.events
```

## 3. Commands

Commands originate in Discord and are normalized:

```text
Discord Interaction
      ↓
Bot Gateway
      ↓
CommandRequest
      ↓
RabbitMQ
      ↓
Service
      ↓
CommandResponse
      ↓
Bot Gateway
      ↓
Discord
```

## 4. Correlation

Every request/event should have:

```text
event_id
correlation_id
causation_id
timestamp
```

This makes distributed debugging possible.

## 5. Delivery semantics

RabbitMQ should be treated as **at-least-once delivery**.

Therefore consumers must be idempotent.

Example:

```text
event_id = 7d...
```

The service stores processed event IDs where necessary.

## 6. Retry policy

Classify failures:

- transient → retry
- permanent validation error → reject
- malformed contract → dead-letter
- dependency unavailable → retry with backoff

Never retry forever.

Use:

```text
main queue
   ↓
retry queue / delayed mechanism
   ↓
main queue
   ↓
dead-letter queue
```

## 7. Command response

Responses should contain machine-readable status:

```json
{
  "correlationId": "…",
  "status": "SUCCESS",
  "message": "Event created",
  "data": {}
}
```

Errors should be structured and safe to display to users.

## 8. Synchronous APIs

REST is appropriate for:

- control panel
- configuration
- administrative queries
- service registration

RabbitMQ is appropriate for:

- Discord events
- asynchronous commands
- background work
- fan-out

Do not force every operation through one transport.
