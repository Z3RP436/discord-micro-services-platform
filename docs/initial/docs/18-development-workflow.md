# 18 – Development Workflow

## 1. Local startup

```bash
./gradlew test
docker compose up -d
./gradlew bootRun
```

The exact run command may be replaced by IDE launch configurations.

## 2. Environment

Use:

```text
application-local.yml
.env.local
```

for developer-specific configuration.

Never commit secrets.

## 3. Feature workflow

```text
Issue
 ↓
Architecture decision if needed
 ↓
Contract
 ↓
Implementation
 ↓
Unit tests
 ↓
Integration tests
 ↓
Documentation
 ↓
Pull request
 ↓
CI
 ↓
Merge
```

## 4. Pull requests

A PR should state:

- what changed
- why it changed
- affected services
- DB migrations
- event contract changes
- security implications
- deployment/rollback notes
- tests performed

## 5. Definition of Done

A feature is done when:

- business behavior is implemented
- tests exist
- observability exists
- configuration is documented
- security implications are handled
- API/event contracts are documented
- migration is safe
- deployment is reproducible

## 6. Breaking changes

Breaking changes require:

- explicit version change
- migration plan
- consumer impact analysis
- rollout order
- rollback strategy

## 7. Local Discord development

Use a dedicated Discord test application and test guild.

Never use production bot tokens for local development.
