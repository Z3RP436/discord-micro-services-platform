# 13 – CI/CD

## 1. Pipeline

Recommended pipeline:

```text
Commit
  ↓
Compile
  ↓
Unit tests
  ↓
Static analysis
  ↓
Integration tests
  ↓
Contract tests
  ↓
Build JARs
  ↓
Build container images
  ↓
Security scan
  ↓
Publish immutable image
  ↓
Deploy staging
  ↓
Smoke tests
  ↓
Production approval/deploy
```

## 2. Versioning

Prefer semantic versioning for services:

```text
MAJOR.MINOR.PATCH
```

Container images should be immutable.

Use:

```text
gamemate:1.4.2
gamemate:sha-<commit>
```

Avoid deploying mutable `latest` tags.

## 3. Database migrations

Use Flyway or Liquibase.

Rules:

- migrations are forward-only
- production migrations are reviewed
- destructive changes require a migration plan
- application and schema compatibility should overlap during rolling deployments

## 4. Deployment strategy

Early:

```text
docker compose pull
docker compose up -d
```

Later:

```text
desired state
    ↓
orchestrator
    ↓
rolling deployment
```

## 5. Rollback

Every release must have a rollback path:

- previous image
- compatible DB migration strategy
- configuration version
- deployment history

## 6. Quality gates

A release should fail when:

- tests fail
- contract compatibility breaks
- critical vulnerabilities are found
- image cannot be reproduced
- required migration checks fail
