# ADR-001: Multiple real Discord bots

- Status: Accepted
- Decision: Each bot is a separate Discord application with its own token and JDA lifecycle.
- Context: The platform must operate multiple independent bot identities.
- Consequences: Token management, bot ownership and connection lifecycle become explicit platform concerns.
