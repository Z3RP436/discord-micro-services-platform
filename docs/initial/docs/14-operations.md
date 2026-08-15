# 14 – Operations and Runbooks

## 1. Backup strategy

Back up:

- PostgreSQL
- platform configuration
- service databases
- critical deployment metadata

Do not back up secrets in plain text.

## 2. Recovery targets

Define explicit targets per deployment.

Example starting point:

```text
RPO: 15 minutes
RTO: 60 minutes
```

These are targets, not guarantees; measure them with restore drills.

## 3. PostgreSQL recovery

Procedure:

1. Stop writes if necessary.
2. Provision clean PostgreSQL.
3. Restore latest backup.
4. Apply required WAL/point-in-time recovery.
5. Validate migrations/schema.
6. Start Platform API.
7. Verify service configuration.
8. Reconcile runtime state.

## 4. RabbitMQ incident

If RabbitMQ is unavailable:

- Discord gateway may continue receiving events temporarily.
- Gateway should buffer only within a bounded capacity.
- Do not allow unbounded in-memory queues.
- Restore broker.
- Publish/recover according to documented delivery policy.
- Inspect dead-letter queues.

## 5. Bot disconnect

Check:

```text
1. Discord status
2. gateway logs
3. credentials/secret reference
4. intents
5. shard ownership
6. rate limits
7. network connectivity
```

The control panel should distinguish `DISCONNECTED`, `STARTING`, `STOPPING`, `FAILED` and `ONLINE`.

## 6. Service crash loop

Check:

```text
1. deployment version
2. configuration
3. database migration
4. dependency availability
5. memory limit
6. recent code changes
```

Use automatic restart with a bounded backoff.

## 7. Dead-letter queue

Every DLQ message should be inspectable by:

```text
eventId
type
version
failure reason
first failure
last failure
consumer
correlationId
```

Replay must be an explicit operator action, not automatic forever.

## 8. Incident process

```text
Detect
  ↓
Triage
  ↓
Contain
  ↓
Recover
  ↓
Validate
  ↓
Document
  ↓
Prevent recurrence
```

Keep a short incident report for production-impacting failures.
