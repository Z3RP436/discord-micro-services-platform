# 15 – Implementation Roadmap

## Phase 0 – Foundation

- [ ] Create monorepo
- [ ] Configure Java 21 and Gradle
- [ ] Add common event contracts
- [ ] Add Docker Compose
- [ ] Start PostgreSQL/RabbitMQ/Redis
- [ ] Add CI build

## Phase 1 – Bot Gateway

- [ ] Implement bot registry
- [ ] Implement secret references
- [ ] Implement JDA lifecycle
- [ ] Implement bot status reporting
- [ ] Normalize Discord events
- [ ] Implement Discord action requests
- [ ] Add rate-limit handling
- [ ] Add metrics and health

## Phase 2 – Platform API

- [ ] Bot CRUD
- [ ] Service registry
- [ ] Guild registry
- [ ] Bot/service assignment
- [ ] Desired-state model
- [ ] Audit logging
- [ ] Authentication/authorization

## Phase 3 – GameMate

- [ ] Extract business logic from existing bot
- [ ] Introduce RabbitMQ consumers
- [ ] Introduce Discord action requests
- [ ] Remove JDA dependency
- [ ] Add GameMate DB
- [ ] Add integration/contract tests

## Phase 4 – Control Panel

- [ ] Angular shell
- [ ] Login
- [ ] Dashboard
- [ ] Bot management
- [ ] Service management
- [ ] Guild configuration
- [ ] Logs/health
- [ ] Audit view

## Phase 5 – Orchestrator

- [ ] Desired/actual reconciliation
- [ ] Docker Engine adapter
- [ ] Service deployment
- [ ] Resource limits
- [ ] Restart policy
- [ ] Deployment history

## Phase 6 – Production Hardening

- [ ] Backups
- [ ] Restore drills
- [ ] Prometheus/Grafana
- [ ] Loki
- [ ] OpenTelemetry
- [ ] Alerting
- [ ] Security scanning
- [ ] Load testing

## Phase 7 – Kubernetes, only if justified

Move to Kubernetes when requirements include:

- multiple hosts
- automated failover
- substantial service count
- frequent deployments
- independent scaling
- stronger scheduling requirements

Do not migrate merely because Kubernetes is popular.
