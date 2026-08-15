# ADR-002: Central Bot Gateway

- Status: Accepted
- Decision: JDA is isolated inside the Bot Gateway.
- Context: Business services should remain independent of Discord/JDA.
- Consequences: Discord events/actions are translated into platform contracts and routed through messaging.
