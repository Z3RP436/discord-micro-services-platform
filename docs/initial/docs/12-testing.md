# 12 – Testing Strategy

## 1. Test pyramid

```text
             E2E
            /   \
      Integration
        /       \
     Component / Contract
       /           \
          Unit
```

Most tests should be unit/component tests.

## 2. Unit tests

Test:

- domain rules
- command validation
- configuration resolution
- permission rules
- state transitions

No Docker or Discord connection required.

## 3. Integration tests

Use Testcontainers for:

- PostgreSQL
- RabbitMQ
- Redis

Example flow:

```text
Service
  ↓
RabbitMQ container
  ↓
Consumer
  ↓
PostgreSQL container
```

## 4. Contract tests

Event contracts are critical.

Test that:

- producers publish valid envelopes
- consumers accept supported versions
- breaking changes are detected in CI

## 5. Bot Gateway tests

Do not depend on a live Discord environment for most tests.

Mock/fake the Discord adapter.

Use a small number of real Discord integration tests in a dedicated test guild.

## 6. End-to-end

A staging environment can validate:

```text
Discord
 → Bot Gateway
 → RabbitMQ
 → GameMate
 → PostgreSQL
 → Bot Gateway
 → Discord
```

## 7. Security tests

Include:

- unauthorized endpoint tests
- role/permission tests
- secret leakage checks
- dependency vulnerability scanning
- container image scanning
- malformed event tests

## 8. Performance tests

Measure:

- event throughput
- command latency
- queue backlog recovery
- database throughput
- bot startup time

Do not optimize before measuring.
