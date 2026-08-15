# 04 – Data, Multi-Tenancy and Security

## 1. Database ownership

Early phase:

```text
PostgreSQL
├── platform schema
└── service schemas/databases
```

Long-term:

```text
Platform DB
GameMate DB
Moderation DB
Music DB
Statistics DB
```

A service owns its domain tables.

The Platform DB must not contain business tables merely for convenience.

## 2. Core platform entities

```text
Bot
Service
BotService
Guild
GuildService
ServiceInstance
Configuration
User
Role
AuditEntry
```

### Relationships

```text
Bot 1 ── N Guild
Bot N ── N Service
Guild N ── N Service
```

## 3. Configuration hierarchy

Recommended precedence:

```text
Platform default
    ↓
Service default
    ↓
Bot configuration
    ↓
Guild configuration
    ↓
Runtime override
```

Only explicitly allowed settings should be overridden at lower levels.

## 4. Secrets

Never store:

- Discord tokens
- OAuth client secrets
- database passwords
- RabbitMQ passwords

in Git.

Recommended progression:

```text
Development → Docker secrets / .env.local
Production → Vault / cloud secret manager / Kubernetes Secrets
```

Database records contain only a secret reference.

## 5. Authentication

Control Panel:

```text
Angular
  ↓ OAuth2/OIDC
Identity Provider
  ↓
Platform API
```

The API validates the identity and maps claims to platform roles.

## 6. Authorization

Example roles:

```text
ADMIN
OPERATOR
SERVICE_MANAGER
BOT_MANAGER
VIEWER
```

Authorization should be resource-aware.

Example:

```text
SERVICE_MANAGER
  can manage services
  cannot rotate bot tokens
```

## 7. Internal authentication

Do not assume internal network traffic is trusted.

Options:

- mTLS
- signed service credentials
- short-lived service tokens

At minimum, use authenticated RabbitMQ credentials and network isolation.

## 8. Audit logging

Administrative changes create immutable audit entries:

```text
actor
action
resource_type
resource_id
old_value
new_value
timestamp
correlation_id
ip / client metadata
```

Never put secrets into audit payloads.

## 9. Input security

Validate:

- command arguments
- guild IDs
- bot IDs
- service IDs
- configuration schemas

Use allowlists for dynamic configuration.

## 10. Data protection

Production PostgreSQL:

- encrypted transport
- restricted network access
- automated backups
- tested restore procedure
- least-privilege DB roles

Redis should be treated as disposable unless a feature explicitly depends on persistence.
