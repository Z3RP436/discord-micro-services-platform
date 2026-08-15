# Architecture Diagrams

All diagrams use Mermaid.

## Overall architecture

```mermaid
flowchart TB
    UI[Angular Control Panel]
    API[Platform API]
    PDB[(Platform PostgreSQL)]
    ORCH[Orchestrator]
    MQ[(RabbitMQ)]
    REDIS[(Redis)]

    subgraph Runtime
      BG[Bot Gateway]
      JDA1[JDA Bot A]
      JDA2[JDA Bot B]
      JDAN[JDA Bot N]
    end

    subgraph Services
      GM[GameMate]
      MOD[Moderation]
      MUSIC[Music]
      STATS[Statistics]
    end

    DISCORD[Discord]

    UI --> API
    API --> PDB
    API --> ORCH
    API --> REDIS
    ORCH --> BG

    BG --> JDA1
    BG --> JDA2
    BG --> JDAN

    JDA1 --> DISCORD
    JDA2 --> DISCORD
    JDAN --> DISCORD

    BG --> MQ
    MQ --> GM
    MQ --> MOD
    MQ --> MUSIC
    MQ --> STATS
```

## Discord event flow

```mermaid
sequenceDiagram
    participant D as Discord
    participant J as JDA
    participant G as Bot Gateway
    participant R as RabbitMQ
    participant S as Service
    participant DB as Service DB

    D->>J: Discord event
    J->>G: JDA event
    G->>R: PlatformEvent
    R->>S: Event delivery
    S->>DB: Domain operation
    S->>R: DiscordActionRequest
    R->>G: Action
    G->>J: JDA call
    J->>D: Discord API
```

## Command flow

```mermaid
sequenceDiagram
    participant D as Discord
    participant G as Bot Gateway
    participant R as RabbitMQ
    participant S as GameMate
    participant DB as GameMate DB

    D->>G: Slash command
    G->>R: CommandRequest
    R->>S: CommandRequest
    S->>DB: Execute use case
    S->>R: CommandResponse
    R->>G: CommandResponse
    G->>D: Interaction response
```

## Desired-state reconciliation

```mermaid
flowchart TD
    A[Desired State in Platform DB]
    B[Reconciler]
    C{Actual State matches?}
    D[Runtime Adapter]
    E[Docker / Kubernetes]
    F[Runtime Instance]

    A --> B
    B --> C
    C -->|Yes| B
    C -->|No| D
    D --> E
    E --> F
    F --> B
```

## Bot ownership

```mermaid
flowchart LR
    B1[Bot A]
    B2[Bot B]
    S1[Gateway Instance 1]
    S2[Gateway Instance 2]

    B1 --> S1
    B2 --> S2
```

The ownership mapping must prevent two active gateway instances from opening the same bot connection unless the Discord/JDA sharding model explicitly allows it.

## Data ownership

```mermaid
flowchart TB
    PA[Platform API] --> PDB[(Platform DB)]
    GM[GameMate] --> GDB[(GameMate DB)]
    MOD[Moderation] --> MDB[(Moderation DB)]
    MUSIC[Music] --> MUDB[(Music DB)]
```
