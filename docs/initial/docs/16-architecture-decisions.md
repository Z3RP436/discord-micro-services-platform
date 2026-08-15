# 16 – Architecture Decision Summary

## ADR-001 – Multiple real Discord bots

**Decision:** Every Discord bot is a separate Discord application with its own token and JDA instance.

**Reason:** Bot identity, permissions, guild access and rate limits are independent.

## ADR-002 – Central Bot Gateway

**Decision:** JDA is isolated in the Bot Gateway.

**Reason:** Prevents every business service from maintaining Discord connections and creates one place for event normalization and Discord actions.

## ADR-003 – RabbitMQ

**Decision:** RabbitMQ is the primary asynchronous transport.

**Reason:** The platform needs routing, queues, acknowledgements, retries and work distribution more than Kafka-style event-stream retention at the current scale.

## ADR-004 – PostgreSQL

**Decision:** PostgreSQL is the primary relational database.

**Reason:** Strong relational consistency, mature tooling and suitability for both platform metadata and service domains.

## ADR-005 – Redis

**Decision:** Redis is a supporting infrastructure component.

**Reason:** Useful for cache, distributed locks, rate limits and idempotency. It is not the primary source of truth.

## ADR-006 – Docker Compose first

**Decision:** Start with Docker Compose.

**Reason:** Low operational overhead and fast local development. Kubernetes is introduced only when the requirements demand it.

## ADR-007 – Desired-state orchestration

**Decision:** Administrative actions modify desired state; a reconciler applies it.

**Reason:** Makes the system resilient to restarts and transient runtime failures.

## ADR-008 – At-least-once messaging

**Decision:** Consumers are idempotent and tolerate duplicate delivery.

**Reason:** Reliability is preferred over pretending exactly-once processing exists across distributed boundaries.

## ADR-009 – Service-owned data

**Decision:** Each business service owns its domain data.

**Reason:** Prevents a central database from becoming a hidden monolith.

## ADR-010 – Contract versioning

**Decision:** Event and command contracts are explicitly versioned.

**Reason:** Services are deployed independently, so compatibility must be managed deliberately.
