# 05 – Infrastructure and Runtime

## 1. Baseline stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Backend | Spring Boot 3.x |
| Discord | JDA 5.x |
| Build | Gradle |
| Messaging | RabbitMQ |
| Database | PostgreSQL |
| Cache/locks | Redis |
| Frontend | Angular |
| Container | Docker |
| Auth | Spring Security + OAuth2/OIDC |
| Metrics | Micrometer + Prometheus |
| Dashboards | Grafana |
| Logs | Loki |
| Tracing | OpenTelemetry |

## 2. Docker Compose topology

```text
postgres
rabbitmq
redis
platform-api
bot-gateway
orchestrator
gamemate-service
moderation-service
...
```

Compose is ideal for development and small installations.

## 3. Network segmentation

Recommended networks:

```text
frontend
  └── platform-api

backend
  ├── platform-api
  ├── orchestrator
  ├── bot-gateway
  └── services

data
  ├── postgres
  ├── rabbitmq
  └── redis
```

Only required ports should be published to the host.

## 4. Container principles

Every service image should:

- run as non-root
- contain no secrets
- have a health check
- use a small runtime image
- expose only required ports
- use immutable version tags in production
- define CPU/memory limits

## 5. Resource model

Example:

```yaml
resources:
  cpu:
    request: 250m
    limit: 1000m
  memory:
    request: 256Mi
    limit: 1024Mi
replicas: 1
```

Docker uses limits; Kubernetes uses requests and limits.

## 6. Orchestration evolution

### Phase 1

```text
Docker Compose
```

### Phase 2

```text
Platform API
   ↓
Orchestrator
   ↓
Docker Engine API
```

### Phase 3

```text
Platform API
   ↓
Orchestrator
   ↓
Kubernetes API
```

The Platform API should not contain Docker-specific implementation details.

## 7. High availability

Do not promise HA merely because multiple containers exist.

For real HA, dependencies also need appropriate redundancy:

- PostgreSQL HA/replication
- RabbitMQ cluster/quorum queues
- Redis HA if required
- multiple gateway instances with explicit bot ownership
- load-balanced API
- external identity provider

For a single-host deployment, document that the host remains a single point of failure.
