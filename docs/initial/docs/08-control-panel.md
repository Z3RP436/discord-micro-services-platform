# 08 – Angular Control Panel

## 1. Responsibilities

The Control Panel is the operational UI for:

- bots
- services
- guilds
- assignments
- desired state
- configuration
- resource limits
- logs
- health
- metrics
- audit
- users and roles

## 2. Angular structure

```text
src/app/
├── core/
│   ├── auth/
│   ├── api/
│   ├── guards/
│   └── interceptors/
├── features/
│   ├── dashboard/
│   ├── bots/
│   ├── services/
│   ├── guilds/
│   ├── deployments/
│   ├── logs/
│   ├── metrics/
│   ├── audit/
│   └── settings/
└── shared/
```

## 3. UX principle

Actions should show desired state and actual state separately.

Example:

```text
GameMate
Desired: RUNNING
Actual:  STARTING
Version: 1.4.0
Replicas: 2
```

Do not show `ONLINE` merely because the desired state is `RUNNING`.

## 4. Dashboard

The dashboard should prioritize operational information:

- services currently failing
- bots disconnected
- queue backlog
- recent deployments
- resource pressure
- active incidents

## 5. API client

Use a typed Angular API layer.

Avoid making raw HTTP calls directly from components.

## 6. Authorization

The UI hides unavailable actions for usability, but the API remains the authoritative authorization boundary.

## 7. Live updates

Potential evolution:

```text
REST → initial state
SSE/WebSocket → live state changes
```

Do not introduce WebSockets until polling becomes insufficient.

## 8. Sensitive values

Never display:

- Discord tokens
- OAuth client secrets
- database passwords

Configuration pages should show references or masked values only.
