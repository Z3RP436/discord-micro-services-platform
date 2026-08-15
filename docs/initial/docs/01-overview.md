# 01 – Architecture Overview

## 1. Purpose

The platform separates Discord connectivity from business functionality.

### Platform responsibilities

- Bot registry
- Service registry
- Guild/service configuration
- Desired runtime state
- Authentication and authorization
- Secret references
- Orchestration
- Audit trail
- Observability
- Administrative API

### Bot Gateway responsibilities

- Start/stop JDA instances
- Maintain Discord sessions
- Receive JDA events
- Normalize Discord events into platform events
- Resolve command routing
- Execute Discord-side actions requested by services
- Enforce bot ownership and connection rules

### Service responsibilities

A service owns business logic and its domain data.

Examples:

- GameMate
- Moderation
- Music
- Statistics
- Tickets
- Events

A service should not depend on JDA classes.

## 2. Dependency direction

```text
Control Panel
     │
     ▼
Platform API ───────► Platform DB
     │
     ▼
Orchestrator ───────► Runtime

Bot Gateway ────────► Discord
     │
     ▼
 RabbitMQ ◄──────────► Services
```

Services consume platform contracts, not JDA internals.

## 3. Desired state vs actual state

The Platform API stores desired state:

```text
bot.main.desired = RUNNING
service.gamemate.desired = RUNNING
service.music.desired = STOPPED
```

Runtime reports actual state:

```text
bot.main.actual = ONLINE
service.gamemate.actual = RUNNING
service.music.actual = STOPPED
```

The reconciler/orchestrator continuously moves actual state toward desired state.

This is preferable to directly treating a button click as the source of truth.

## 4. Multi-bot model

Each bot is a real Discord application:

```text
Bot A
 ├── Discord Application
 ├── Token A
 ├── JDA A
 └── assigned guilds

Bot B
 ├── Discord Application
 ├── Token B
 ├── JDA B
 └── assigned guilds
```

A service may be enabled for multiple bots:

```text
Main Bot ── GameMate
Gaming Bot ── GameMate
Event Bot ── Events
```

The service implementation exists once.

## 5. Multi-guild model

Service enablement is evaluated at guild level:

```text
Bot
 └── Guild
      └── Service Configuration
```

This makes it possible to run the same service with different configuration per Discord server.

## 6. Architectural principles

1. **Single responsibility** – JDA lives in the gateway; business logic lives in services.
2. **Explicit contracts** – messages are versioned platform contracts.
3. **Asynchronous by default** – cross-service work uses RabbitMQ unless a synchronous request is genuinely required.
4. **Idempotency** – every externally triggered operation must tolerate retries.
5. **Desired-state management** – administration changes desired state; reconciliation applies it.
6. **Least privilege** – services and users receive only required permissions.
7. **Observable by default** – health, metrics, logs and traces are part of every service.
8. **Deploy independently** – a service should be buildable and deployable without changing other services.
9. **No secret leakage** – tokens never appear in Git, normal logs or API responses.
10. **Evolution over premature complexity** – Docker Compose first, orchestrator second, Kubernetes when justified.

## 7. Failure boundaries

A RabbitMQ outage must not cause every service to crash.

A single service crash must not disconnect all Discord bots.

A database outage should fail closed for administration but should not necessarily terminate established Discord sessions.

A Discord outage should not make the Platform API unavailable.

These boundaries are important when defining retries and health checks.
