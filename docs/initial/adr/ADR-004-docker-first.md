# ADR-004: Docker Compose before Kubernetes

- Status: Accepted
- Decision: Start with Docker Compose and introduce a runtime adapter/orchestrator before considering Kubernetes.
- Context: The project should minimize operational complexity while the architecture is validated.
- Consequences: The platform must abstract runtime operations so Docker-specific APIs do not leak into business logic.
