# 02 – Multiple Bots and JDA

## 1. Bot identity

Every bot record contains:

```text
id
name
token_secret_ref
status
desired_state
shard_count
intents
created_at
updated_at
```

The token is never stored directly in the domain model.

## 2. JDA lifecycle

The Bot Gateway owns:

```java
Map<BotId, JDA>
```

A simplified lifecycle:

```text
DESIRED RUNNING
      │
      ▼
Load secret
      │
      ▼
Create JDA
      │
      ▼
Register listeners
      │
      ▼
Await READY
      │
      ▼
ONLINE
```

Stopping:

```text
STOP REQUEST
   │
   ▼
Stop accepting new work
   │
   ▼
JDA.shutdown()
   │
   ▼
OFFLINE
```

## 3. Important scaling rule

Do not blindly run two identical Bot Gateway replicas with the same bot token.

Possible production strategies:

### Strategy A – single gateway replica

Best for early deployments.

```text
bot-gateway
 ├── JDA Main
 ├── JDA Gaming
 └── JDA Events
```

### Strategy B – explicit bot ownership

Assign bots to gateway instances:

```text
gateway-1 → Main, Events
gateway-2 → Gaming, Test
```

The orchestrator is responsible for ownership.

### Strategy C – Discord sharding

For very large bots, shard one Discord application according to Discord/JDA requirements. Shards become the unit of ownership.

The architecture should therefore distinguish:

- bot
- shard
- gateway instance

## 4. Event normalization

JDA-specific events must be converted at the boundary.

Bad:

```java
rabbitTemplate.convertAndSend(new GuildMemberJoinEvent(...));
```

Good:

```java
new PlatformEvent(
    id,
    "discord.member.joined",
    1,
    botId,
    guildId,
    timestamp,
    payload
);
```

Services receive stable contracts instead of JDA classes.

## 5. Discord actions

Services should not call JDA.

Instead:

```text
GameMate
  │
  │ DiscordActionRequest
  ▼
RabbitMQ
  │
  ▼
Bot Gateway
  │
  ▼
JDA
  │
  ▼
Discord
```

Examples:

- send message
- edit message
- delete message
- assign role
- remove role
- create thread
- create channel

## 6. Rate limiting

Discord rate limits must be handled centrally where possible.

The gateway should expose:

- queueing
- retry-after handling
- per-route limits
- metrics
- failure classification

Business services should not implement Discord HTTP rate-limit logic themselves.

## 7. Intents

Each bot should request only the intents it needs.

Store intended intents as configuration and validate them during startup.
