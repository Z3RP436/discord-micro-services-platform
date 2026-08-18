rootProject.name = "discord-micro-services-platform"

include(
    ":contracts:bot-contracts",
    ":projects:bot-core",
    ":projects:bot-api",
    ":projects:bot-orchestrator",
    ":projects:bot-main",
    ":projects:bot-identity",
    ":projects:bot-logging"
)