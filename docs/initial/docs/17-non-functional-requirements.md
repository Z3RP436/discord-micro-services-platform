# 17 – Non-Functional Requirements

The following are recommended starting targets. They must be validated against the actual deployment.

## Availability

### Platform API

Target for a small production deployment:

```text
99.5% monthly availability
```

### Bot Gateway

The target depends on deployment topology. A single host remains a single point of failure.

## Performance

Initial targets:

```text
Control API p95 < 500 ms for normal CRUD
Internal command routing p95 < 1 s excluding Discord rate limits
Event consumer processing p95 < 2 s for normal workloads
```

These exclude external Discord outages and intentional backpressure.

## Scalability

The system should scale independently in:

- API replicas
- service replicas
- worker consumers
- bot gateway instances
- Discord shards

## Security

Mandatory:

- authenticated administration
- RBAC
- TLS in production
- secret isolation
- audit logging
- dependency scanning
- container scanning
- least-privilege service accounts

## Reliability

Mandatory:

- bounded retries
- dead-letter queues
- idempotent consumers
- health checks
- graceful shutdown
- database backups
- restore testing

## Maintainability

A new Discord feature should require:

1. creating a service or extending an existing domain service
2. declaring event/command contracts
3. implementing business logic
4. registering configuration
5. adding tests
6. deploying the service

It should **not** require adding another JDA connection.

## Operability

An operator must be able to answer:

- Is the bot connected?
- Which service is failing?
- Which version is deployed?
- Are messages backed up?
- Which guilds have the service enabled?
- Who changed the configuration?
- Can the previous version be restored?
