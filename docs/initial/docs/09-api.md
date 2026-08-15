# 09 – Platform API

## 1. API style

Base path:

```text
/api/v1
```

Use resource-oriented REST for administrative state.

## 2. Bots

```http
GET    /api/v1/bots
POST   /api/v1/bots
GET    /api/v1/bots/{botId}
PATCH  /api/v1/bots/{botId}
DELETE /api/v1/bots/{botId}

POST   /api/v1/bots/{botId}/actions/start
POST   /api/v1/bots/{botId}/actions/stop
POST   /api/v1/bots/{botId}/actions/restart
```

## 3. Services

```http
GET    /api/v1/services
POST   /api/v1/services
GET    /api/v1/services/{serviceId}
PATCH  /api/v1/services/{serviceId}

POST   /api/v1/services/{serviceId}/actions/start
POST   /api/v1/services/{serviceId}/actions/stop
POST   /api/v1/services/{serviceId}/actions/restart
POST   /api/v1/services/{serviceId}/actions/scale
```

## 4. Assignments

```http
GET  /api/v1/bots/{botId}/services
PUT  /api/v1/bots/{botId}/services/{serviceId}

GET  /api/v1/guilds/{guildId}/services
PUT  /api/v1/guilds/{guildId}/services/{serviceId}
```

## 5. Runtime state

```http
GET /api/v1/runtime/bots
GET /api/v1/runtime/services
GET /api/v1/runtime/instances
```

Runtime state is read-only from the normal administrative API.

## 6. Error format

Use a stable problem response:

```json
{
  "type": "https://platform.example/errors/service-not-found",
  "title": "Service not found",
  "status": 404,
  "code": "SERVICE_NOT_FOUND",
  "detail": "The requested service does not exist.",
  "correlationId": "..."
}
```

## 7. Idempotency

Mutating endpoints that can be retried should support:

```http
Idempotency-Key: <uuid>
```

The server stores the result for a bounded period.

## 8. OpenAPI

The Platform API should publish an OpenAPI specification and generate the Angular client from that contract where practical.

## 9. Concurrency

Use optimistic locking for configuration/state records.

Example:

```text
version = 17
```

A write based on an older version receives a conflict instead of silently overwriting newer state.
