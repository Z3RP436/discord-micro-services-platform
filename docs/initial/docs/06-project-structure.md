# 06 – Repository and Gradle Structure

## 1. Recommended monorepo

```text
discord-platform/
├── settings.gradle
├── build.gradle
├── gradle.properties
├── gradlew
├── docker-compose.yml
├── .github/
│   └── workflows/
├── docs/
├── examples/
│
├── platform-common/
├── platform-api/
├── bot-gateway/
├── orchestrator/
├── gamemate-service/
├── moderation-service/
├── music-service/
└── statistics-service/
```

## 2. Shared module rule

`platform-common` may contain:

- event envelope
- command envelope
- IDs
- shared serialization
- error codes

It must **not** become a dumping ground for business logic.

Prefer versioned contract modules if shared dependencies become too tightly coupled.

## 3. Service internal structure

```text
gamemate-service/
└── src/main/java/...
    └── gamemate/
        ├── api/
        ├── application/
        ├── domain/
        ├── infrastructure/
        ├── messaging/
        └── configuration/
```

Suggested responsibilities:

```text
api             REST/internal adapters
application      use cases
domain           business rules
infrastructure   PostgreSQL/external implementations
messaging        RabbitMQ consumers/producers
configuration    Spring configuration
```

## 4. Gradle

```groovy
rootProject.name = 'discord-platform'

include 'platform-common'
include 'platform-api'
include 'bot-gateway'
include 'orchestrator'
include 'gamemate-service'
include 'moderation-service'
include 'music-service'
include 'statistics-service'
```

Use a version catalog for dependency versions as the project grows.

## 5. Naming

Use stable technical IDs:

```text
gamemate
moderation
music
statistics
```

Do not use display names as routing identifiers.

## 6. Versioning

Services have independent versions.

Example:

```text
gamemate: 1.4.0
moderation: 2.1.3
```

Platform contracts have independent schema versions:

```text
discord.member.joined.v1
discord.member.joined.v2
```
