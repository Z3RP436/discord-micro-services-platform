# Changelog

## Unreleased

- BREAKING CLEANUP: Removed the template-bot concept; the platform now lists only real runnable bot services.
- `bot-main` is treated as a real bot service and follows the same JDA token requirement as other bots.
- Start guard reason codes are standardized to `TOKEN_MISSING`, `TOKEN_INVALID`, and `BOT_NOT_RUNNABLE`.

## 1.0.0 – 2026-08-14

- Expanded the original architecture into a project-level documentation set.
- Added architecture principles and boundaries.
- Added API, event contracts and versioning.
- Added security, observability, testing, CI/CD and operations.
- Added NFRs, development workflow and ADRs.
- Added Mermaid diagrams and example manifests.
