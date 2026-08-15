# 11 – Observability

## 1. Three pillars

```text
Metrics
Logs
Traces
```

Use OpenTelemetry where practical so instrumentation is not tied to one backend.

## 2. Health

Every Spring service should expose:

```text
/actuator/health
/actuator/health/liveness
/actuator/health/readiness
/actuator/prometheus
```

Readiness should reflect whether the service can actually process work.

## 3. Metrics

### Bot Gateway

```text
discord.gateway.connections
discord.gateway.disconnects
discord.events.received
discord.events.failed
discord.actions.sent
discord.actions.failed
discord.rate_limit.wait_seconds
```

### RabbitMQ

```text
messaging.messages.published
messaging.messages.consumed
messaging.messages.failed
messaging.consumer.lag
messaging.dead_letters
```

### Services

```text
service.commands.executed
service.commands.failed
service.processing.duration
service.db.query.duration
```

### Runtime

```text
process.cpu
process.memory
jvm.memory.used
jvm.gc.pause
container.restarts
```

## 4. Logging

Use structured JSON logs.

Every log should include where possible:

```text
timestamp
level
service
version
instance
traceId
spanId
correlationId
message
```

Never log:

- tokens
- passwords
- authorization headers
- sensitive user data unnecessarily

## 5. Tracing

Trace:

```text
Discord event
 → gateway
 → RabbitMQ publish
 → service consumer
 → database
 → Discord action
```

RabbitMQ propagation should preserve trace context.

## 6. Alerting

Initial alerts:

- bot disconnected unexpectedly
- service crash loop
- queue backlog above threshold
- dead-letter queue receives messages
- PostgreSQL unavailable
- RabbitMQ unavailable
- high error rate
- high memory usage
- repeated deployment failure

## 7. Dashboards

Recommended dashboards:

1. Platform overview
2. Discord bots
3. RabbitMQ
4. JVM/services
5. Infrastructure
6. Deployments
