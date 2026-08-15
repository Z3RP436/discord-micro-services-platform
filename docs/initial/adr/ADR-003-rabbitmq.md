# ADR-003: RabbitMQ as asynchronous transport

- Status: Accepted
- Decision: RabbitMQ is the initial event and work transport.
- Context: The platform requires routing, acknowledgements, retries and consumer isolation.
- Consequences: Consumers must be idempotent and dead-letter handling is required.
