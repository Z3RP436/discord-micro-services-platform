# 10 – Event Contracts

## 1. Envelope

Every message uses a common envelope.

```json
{
  "eventId": "uuid",
  "type": "discord.member.joined",
  "version": 1,
  "occurredAt": "2026-08-14T18:00:00Z",
  "correlationId": "uuid",
  "causationId": "uuid",
  "botId": "main",
  "guildId": "123456789",
  "payload": {}
}
```

## 2. Required fields

| Field | Purpose |
|---|---|
| eventId | Unique message identity |
| type | Stable routing/type name |
| version | Contract version |
| occurredAt | Source timestamp |
| correlationId | End-to-end request tracing |
| causationId | Links derived actions/events |
| botId | Discord bot identity |
| guildId | Discord server context where applicable |
| payload | Event-specific data |

## 3. Naming

Use:

```text
discord.member.joined
discord.member.left
discord.message.created
discord.message.deleted
discord.interaction.command
discord.guild.created
platform.service.started
platform.service.stopped
```

## 4. Compatibility

Prefer additive changes.

Safe:

```json
{
  "userId": "...",
  "displayName": "...",
  "avatarUrl": "..."
}
```

Adding a new optional field is normally backward-compatible.

Breaking changes require a new version:

```text
discord.member.joined.v2
```

## 5. Event ownership

The component closest to the source owns source events.

The Bot Gateway publishes Discord events.

Business services publish business events.

The Platform API publishes administrative events where useful.

## 6. Security

Never put secrets or unnecessary personal data into events.

Payloads should contain only what consumers need.

## 7. Correlation example

```text
Discord interaction
correlationId = A

CommandRequest
correlationId = A
causationId = DiscordEventId

GameMate event
correlationId = A
causationId = CommandRequestId

DiscordActionRequest
correlationId = A
causationId = GameMateEventId
```

This gives a complete trace across services.
