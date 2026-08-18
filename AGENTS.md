# AGENTS.md

## Project

This repository contains the `discord-micro-services-platform`, a Java-based
microservices platform for Discord applications.

The project is structured as a Gradle multi-project build.

Main areas:

- `projects/` - executable backend services
- `contracts/` - shared contracts between services
- `frontend/` - frontend applications
- `infrastructure/` - local and deployment infrastructure
- `docs/` - project documentation
- `memory/` - agent-maintained project memory

---

## Current Approved Target Vision (2026-08-18)

The current repository state before this date is considered an early draft.
Agents should align implementation tasks with the following approved target.

### Product Goal

Build a Discord bot microservices platform where:

- bots can be developed independently without changing the main core framework
- a React frontend can control bot lifecycle and profile assignment
- communication is event-driven through RabbitMQ
- delivery runs through GitHub CI/CD and Docker

### Approved Architecture Decisions

- Discovery model: Hybrid GitOps catalog + RabbitMQ live status
- Naming model:
  - `bot-api`
  - `bot-core`
  - `bot-orchestrator`
  - `bot-main`
  - `bot-identity`
  - `bot-logging`
- Core strategy: Hybrid library + plugin extension points
- Resource model: static defaults with UI override per bot/guild
- Persistence: Postgres for config/permission-related data
- Bot model: no template service concept; only real bot services including `bot-main`
- Token policy: every startable bot (including `bot-main`) requires a stored JDA token
- API policy: OpenAPI-first with interface contracts derived from the v1 specs
- Infrastructure first: Docker Compose (before production orchestration)
- Delivery scope: full MVP base including CI workflow drafts

### Boundary Rules Derived From Approved Vision

- `bot-core` should remain a reusable shared foundation and extension API.
- Bot-specific behavior should live in dedicated bot services.
- New bot functionality should not require core changes unless the extension contract itself must change.
- Cross-service payloads must stay in shared contracts.
- Keep service boundaries explicit and domain-driven.

---

## Core Rules

### 1. Do not implement unapproved changes

The agent MUST NOT independently implement features, architectural changes,
refactorings, dependencies, configuration changes, documentation changes, or
infrastructure changes that were not explicitly requested or agreed upon.

If a requested task reveals additional work that appears necessary:

1. Stop before implementing it.
2. Explain what is additionally required.
3. Ask for approval.
4. Only implement it after approval.

Do not interpret "make X work" as permission to redesign surrounding systems.

A technically better solution is not automatically an authorized solution.

---

### 2. When uncertain, ask

If the intended behavior, architecture, scope, or implementation approach is
ambiguous, do not guess.

Ask a concise clarification question before implementing.

The user makes architectural and scope decisions.

---

### 3. Plan before implementation

For non-trivial tasks:

1. Understand the existing project structure.
2. Inspect only the files relevant to the task.
3. Briefly describe the planned changes.
4. Identify architectural or scope decisions.
5. Wait for approval if the change introduces architectural decisions or
   significant additional scope.
6. Implement only the agreed changes.

Do not perform exploratory modifications just to "see what works".

---

### 4. Minimize unnecessary work and credit usage

The agent should optimize for low token/credit consumption.

Rules:

- Do not read the entire repository when only a few files are relevant.
- Do not repeatedly inspect files that have not changed.
- Prefer targeted searches over broad repository scans.
- Do not run unnecessary commands.
- Do not repeat builds/tests that already passed unless relevant changes were made.
- Do not perform speculative research or implementation.
- Do not generate verbose explanations when a concise explanation is sufficient.
- Group related inspections and commands where practical.
- Reuse information already established in the conversation.
- Before using a tool, consider whether the information is already available.
- Prefer existing project documentation and memory over rediscovering known facts.

Correctness takes priority over minimizing credit usage.

---

## Code Quality

### Clean Code

All implementation MUST follow Clean Code principles.

Prefer:

- meaningful names
- small, focused classes and methods
- single responsibility
- low coupling
- high cohesion
- clear abstractions
- explicit dependencies
- minimal side effects
- readable code over clever code

Avoid:

- unnecessary complexity
- premature abstractions
- duplicated logic
- magic numbers and strings
- deeply nested control flow
- god classes
- god methods
- speculative extensibility
- dead code
- commented-out code

Code should be easy to understand without requiring extensive comments.

Comments should explain why something exists, not simply repeat what the code
does.

---

### SOLID

Apply SOLID principles where they improve the design.

Do not force abstractions or design patterns solely to demonstrate SOLID.

Use the simplest design that satisfies the actual requirement.

---

### YAGNI

Do not implement functionality "for later".

If a feature is not currently required, do not add it unless explicitly requested.

Examples:

- no unused interfaces
- no unused configuration
- no unused database tables
- no speculative APIs
- no future-proof abstractions without a current use case

---

### DRY

Avoid duplication, but do not create abstractions merely because two pieces
of code currently look similar.

Abstraction should be introduced when there is a real shared responsibility.

---

## Architecture

The project follows a microservices architecture.

Services must have clear responsibilities and boundaries.

Do not introduce direct dependencies between services unless explicitly
approved.

Communication between services should use the project's defined contracts
and communication mechanisms.

Architecture changes require explicit approval.

The agent MUST NOT:

- introduce a new service without approval
- merge service responsibilities
- move functionality between services without approval
- introduce a new infrastructure component without approval
- change communication patterns without approval
- introduce a new framework or library without approval
- change the persistence architecture without approval
- change service boundaries without approval

---

## Dependencies

Do not add, remove, or upgrade dependencies without explicit approval.

When a dependency is required for an approved task:

1. Explain why it is needed.
2. Prefer established project dependencies.
3. Use the version-management strategy already defined by the project.
4. Avoid adding libraries for functionality that can reasonably be implemented
   with existing dependencies or the JDK.

Do not introduce multiple libraries for the same responsibility.

---

## Gradle

The project uses Gradle with the Gradle Wrapper.

Always prefer the Gradle Wrapper over a globally installed Gradle version.

On Windows:

```text
.\gradlew.bat
```

On Unix-like systems:

```text
./gradlew
```

The project currently uses:

- Java 24
- Gradle Wrapper
- Kotlin DSL (`*.gradle.kts`)
- Gradle Version Catalogs
- centralized dependency/version management

Do not change the Java or Gradle version without approval.

Do not introduce a second dependency/version management mechanism.

---

## Testing

Tests are part of the implementation.

For changes affecting behavior:

1. Add or update relevant tests.
2. Run the smallest relevant test/build command first.
3. Run broader verification only when necessary.

Do not create tests solely to increase coverage numbers.

Tests should verify behavior, not implementation details.

A successful build does not replace relevant tests.

---

## Git

Use feature branches for changes.

Do not commit directly to `main` unless explicitly requested.

Commit messages should follow Conventional Commits.

Examples:

```text
feat: add bot connection management
fix: handle failed discord connection
refactor: simplify authorization flow
test: add authorization service tests
docs: document service communication
chore: update gradle configuration
```

Do not create commits automatically unless explicitly requested.

Do not push changes automatically unless explicitly requested.

Do not merge pull requests automatically.

Do not rewrite shared Git history unless explicitly requested.

---

## Documentation

All project documentation MUST be stored under `docs/`.

Documentation is strictly grouped by domain:

```text
docs/
├── architecture/
├── development/
├── deployment/
└── operations/
```

Do not place project documentation directly in `docs/`.

Do not create additional documentation categories without approval.

### Documentation naming

Normal documentation follows:

```text
<domain>-<topic>.md
```

Examples:

```text
architecture-overview.md
architecture-service-boundaries.md

development-setup.md
development-gradle.md

deployment-docker.md
deployment-production.md

operations-monitoring.md
operations-troubleshooting.md
```

### Architecture Decision Records

Architecture decisions use:

```text
ADR-XXXX-<short-description>.md
```

Examples:

```text
ADR-0001-microservices-architecture.md
ADR-0002-bot-gateway-responsibility.md
ADR-0003-inter-service-communication.md
```

ADRs MUST document significant architectural decisions and their reasoning.

Do not create an ADR for trivial implementation details.

### Project and development logs

Logs use:

```text
YYYY-MM-DD-<domain>-<topic>.md
```

Examples:

```text
2026-08-15-development-project-initialization.md
2026-08-15-architecture-service-boundaries.md
```

Logs should contain meaningful project information.

Do not create logs for every individual agent action.

Do not create documentation merely to increase the number of documents.

---

## Agent Memory

Agent memory is stored separately from project documentation under `memory/`.

Memory is strictly grouped by domain:

```text
memory/
├── architecture/
│   └── MEMORY.md
├── development/
│   └── MEMORY.md
├── deployment/
│   └── MEMORY.md
└── operations/
    └── MEMORY.md
```

### Reading memory

Before starting a task, the agent should read only the memory files relevant
to the requested task.

Do not read all memory files by default.

Memory exists to preserve concise, reusable project context and reduce
unnecessary repository exploration.

### Writing memory

The agent MAY update relevant memory after completing an approved task when
the task establishes durable project knowledge.

Useful memory includes:

- confirmed architectural decisions
- established project conventions
- important tooling decisions
- recurring development procedures
- known operational constraints
- important service responsibilities
- confirmed project-wide rules

### Do not store

Memory MUST NOT contain:

- secrets
- credentials
- API keys
- tokens
- conversation transcripts
- temporary task state
- speculative ideas
- unapproved decisions
- redundant information
- information that can easily be derived from the source code

### Memory authority

Memory is contextual information, not authorization.

The agent MUST NOT implement functionality merely because it is mentioned in
memory.

Explicit user instructions and approved project decisions always take
precedence over memory.

### Keeping memory useful

Memory should remain concise.

When information becomes outdated:

1. update it
2. remove obsolete information
3. do not keep historical information unless it remains relevant

Do not append duplicate entries.

The agent should prefer updating an existing memory entry over creating a new
one when the information belongs to the same topic.

---

## Configuration and Secrets

Never commit secrets.

Examples:

- Discord bot tokens
- API keys
- passwords
- private keys
- production credentials
- database credentials

Use environment variables or approved configuration mechanisms.

Never invent credentials or placeholder secrets that could be mistaken for
real credentials.

---

## Scope Control

The agent MUST stay within the requested scope.

If unrelated problems are discovered:

1. Mention them briefly.
2. Do not fix them automatically.
3. Continue only with the requested task.

If an unrelated problem prevents the requested task from working:

1. Explain the blocker.
2. Describe the required additional change.
3. Ask for approval.
4. Only then make the additional change.

---

## Decision Policy

When multiple technically valid approaches exist:

- prefer the simplest solution
- prefer existing project conventions
- prefer standard Java/Spring/Gradle functionality
- prefer existing dependencies
- avoid unnecessary dependencies
- avoid unnecessary abstractions
- avoid speculative functionality

If the choice affects:

- architecture
- security
- persistence
- service boundaries
- inter-service communication
- infrastructure
- public APIs
- dependency strategy

ask for approval before implementing the decision.

---

## Agent Behavior

The agent is an implementation assistant, not an autonomous architect.

The user makes architectural and scope decisions.

The agent may:

- analyze
- suggest
- explain
- identify problems
- propose alternatives
- inspect relevant files
- implement explicitly approved changes
- run relevant verification
- update relevant agent memory when appropriate

The agent must not:

- silently expand scope
- make major architectural decisions independently
- implement "helpful" additional features
- refactor unrelated code
- add speculative functionality
- change dependencies without approval
- change infrastructure without approval
- commit without approval
- push without approval
- merge without approval
