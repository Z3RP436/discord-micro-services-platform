# Deployment Memory

This file contains concise, durable deployment-related project context.

## Confirmed Decisions

- Local-first deployment baseline: Docker Compose.
- Messaging infrastructure: RabbitMQ.
- Persistence baseline: Postgres.
- CI/CD direction: GitHub Actions workflows (draft stage in MVP foundation).

## Rules

- Keep entries concise.
- Store only confirmed project knowledge.
- Do not store secrets or temporary task state.
- Prefer updating existing entries over creating duplicates.
