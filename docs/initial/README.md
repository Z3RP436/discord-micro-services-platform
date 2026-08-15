# Discord Platform – Project Documentation

A modular, event-driven platform for operating multiple real Discord bots and independently deployable Discord features.

## Goals

- Run **multiple real Discord applications**, each with its own token and JDA connection.
- Keep Discord/JDA concerns centralized in a **Bot Gateway**.
- Implement business capabilities as independent **Spring Boot services**.
- Route Discord events and command requests through **RabbitMQ**.
- Manage bots, services, guild assignments, configuration and desired runtime state through a **Platform API** and Angular Control Panel.
- Start small with Docker Compose and evolve toward an orchestrator/Kubernetes only when operational complexity justifies it.
- Provide production-grade security, observability, testing, CI/CD, auditing and disaster recovery.

## Documentation map

| Document | Purpose |
|---|---|
| [01-overview.md](docs/01-overview.md) | Architecture, boundaries and principles |
| [02-bots-and-jda.md](docs/02-bots-and-jda.md) | Multiple Discord bots, JDA and lifecycle |
| [03-services-and-events.md](docs/03-services-and-events.md) | Services, events, commands and messaging |
| [04-data-and-security.md](docs/04-data-and-security.md) | Data ownership, security and multi-tenancy |
| [05-infrastructure.md](docs/05-infrastructure.md) | Runtime, Docker, networking and dependencies |
| [06-project-structure.md](docs/06-project-structure.md) | Repository and Gradle structure |
| [07-gamemate.md](docs/07-gamemate.md) | GameMate as reference service |
| [08-control-panel.md](docs/08-control-panel.md) | Angular administration UI |
| [09-api.md](docs/09-api.md) | Platform API conventions and endpoints |
| [10-event-contracts.md](docs/10-event-contracts.md) | Event envelope, routing and compatibility |
| [11-observability.md](docs/11-observability.md) | Metrics, logs, tracing and alerting |
| [12-testing.md](docs/12-testing.md) | Test strategy |
| [13-ci-cd.md](docs/13-ci-cd.md) | Build, image publishing and deployment |
| [14-operations.md](docs/14-operations.md) | Runbooks, backups and incident handling |
| [15-roadmap.md](docs/15-roadmap.md) | Implementation phases |
| [16-architecture-decisions.md](docs/16-architecture-decisions.md) | Decision summary |
| [17-non-functional-requirements.md](docs/17-non-functional-requirements.md) | Availability, performance and security targets |
| [18-development-workflow.md](docs/18-development-workflow.md) | Local development and contribution workflow |
| [diagrams.md](docs/diagrams.md) | Mermaid architecture diagrams |

## Architecture in one picture

```text
                         ┌─────────────────────┐
                         │ Angular Control     │
                         │ Panel               │
                         └──────────┬──────────┘
                                    │ HTTPS
                                    ▼
                         ┌─────────────────────┐
                         │ Platform API        │
                         │ Spring Boot         │
                         └───────┬─────┬───────┘
                                 │     │
                     PostgreSQL  │     │ Desired State
                                 ▼     ▼
                           ┌────────┐ ┌──────────────┐
                           │Platform│ │ Orchestrator │
                           │  DB    │ └──────┬───────┘
                           └────────┘        │
                                             ▼
                                      Docker / K8s
                                             │
                         ┌───────────────────┴───────────────────┐
                         │                                       │
                 ┌───────▼────────┐                    ┌─────────▼────────┐
                 │  Bot Gateway   │                    │ Business Services │
                 │ JDA x N        │                    │ GameMate          │
                 │ Bot A/B/C      │                    │ Moderation        │
                 └───────┬────────┘                    │ Music / Stats ... │
                         │                             └─────────▲─────────┘
                         │                                       │
                         ▼                                       │
                      Discord                               RabbitMQ
                         ▲                                       ▲
                         └──────────── Discord events ───────────┘
```

## Core invariant

**One Discord bot token must have one logical JDA connection per active shard/assignment.**

The platform may run many bots, but it must not accidentally start duplicate connections for the same token. Horizontal scaling of the Bot Gateway therefore requires an explicit ownership/sharding strategy.

## Technology baseline

- Java 21
- Spring Boot 3.x
- JDA 5.x
- Gradle
- PostgreSQL
- RabbitMQ
- Redis
- Docker
- Angular
- Spring Security + OAuth2/OIDC
- Micrometer + Prometheus
- Grafana
- Loki
- OpenTelemetry

Kubernetes is an evolution step, not a prerequisite.
