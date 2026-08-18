Responsibility boundaries
bot/
Core Discord infrastructure
Shared bot functionality
Discord/JDA integration
Bot lifecycle
Command/event routing
Cross-service bot adapters


services/
Actual service implementations
Service-specific business logic
Service-specific persistence
Service-specific APIs
Service-specific messaging logic


contracts/
Communication contracts only
Events
Commands
Schemas


infrastructure/
Local/runtime infrastructure
Docker Compose
RabbitMQ
PostgreSQL
Supporting infrastructure

A service must never become part of the Core Bot implementation.

2. Repository Structure
   discord-platform/
   │
   ├── settings.gradle
   ├── build.gradle
   ├── gradle.properties
   ├── gradlew
   ├── gradlew.bat
   │
   ├── gradle/
   │   └── libs.versions.toml
   │
   ├── .github/
   │   └── workflows/
   │       ├── build.yml
   │       ├── test.yml
   │       └── ...
   │
   ├── docs/
   │   ├── architecture/
   │   ├── api/
   │   ├── contracts/
   │   └── development/
   │
   ├── examples/
   │
   ├── bot/
   │   │
   │   ├── core/
   │   │   └── src/
   │   │       ├── main/
   │   │       │   ├── java/
   │   │       │   │   └── ...
   │   │       │   └── resources/
   │   │       │       └── application.yml
   │   │       └── test/
   │   │
   │   └── shared/
   │       ├── discord/
   │       ├── commands/
   │       ├── events/
   │       ├── permissions/
   │       ├── localization/
   │       ├── configuration/
   │       └── utilities/
   │
   ├── contracts/
   │   │
   │   ├── events/
   │   │   └── src/
   │   │
   │   └── commands/
   │       └── src/
   │
   ├── services/
   │   │
   │   ├── gamemate/
   │   │   ├── src/
   │   │   │   ├── main/
   │   │   │   │   ├── java/
   │   │   │   │   └── resources/
   │   │   │   └── test/
   │   │   └── build.gradle
   │   │
   │   ├── moderation/
   │   │   ├── src/
   │   │   └── build.gradle
   │   │
   │   ├── music/
   │   │   ├── src/
   │   │   └── build.gradle
   │   │
   │   └── statistics/
   │       ├── src/
   │       └── build.gradle
   │
   ├── infrastructure/
   │   ├── docker-compose.yml
   │   ├── rabbitmq/
   │   ├── postgres/
   │   └── ...
   │
   └── README.md

The exact number of services may grow over time. The architectural boundaries must remain unchanged.

3. Bot Layer

The bot/ directory contains everything that belongs to the Discord Bot platform itself.

It does not contain the implementation of individual business services.

bot/
├── core/
└── shared/

This distinction is intentional.

4. Bot Core

The Core Bot is responsible for the fundamental Discord runtime.

bot/core/
└── src/
└── main/
└── java/
└── discord/
└── platform/
└── bot/
└── core/
├── BotApplication.java
│
├── lifecycle/
│   ├── BotLifecycle.java
│   └── ShutdownHandler.java
│
├── jda/
│   ├── JdaFactory.java
│   ├── JdaConfiguration.java
│   └── JdaEventBridge.java
│
├── gateway/
│   ├── GatewayManager.java
│   └── GatewayConfiguration.java
│
├── registry/
│   ├── CommandRegistry.java
│   ├── EventRegistry.java
│   └── ServiceRegistry.java
│
├── routing/
│   ├── CommandRouter.java
│   ├── EventRouter.java
│   └── InteractionRouter.java
│
└── configuration/
└── CoreBotConfiguration.java
Core responsibilities

The Core Bot handles things such as:

JDA initialization
Discord connection
Bot lifecycle
Gateway handling
Discord event reception
Interaction routing
Command registration
Event registration
Global bot configuration
Graceful shutdown
Core error handling

The Core does not know how GameMate, Moderation, Music, or Statistics work.

For example, the Core may know that a command exists:

/gamemate

but it must not contain GameMate's business logic.

5. Shared Bot Code

The bot/shared/ area contains functionality that is reusable across the Discord Bot ecosystem.

This is Bot infrastructure, not service business logic.

bot/shared/
├── discord/
├── commands/
├── events/
├── permissions/
├── localization/
├── configuration/
└── utilities/
5.1 Discord
bot/shared/discord/
├── DiscordChannelService.java
├── DiscordGuildService.java
├── DiscordMemberService.java
├── DiscordRoleService.java
└── DiscordMessageService.java

Reusable Discord operations belong here.

Examples:

Resolve guild
Resolve member
Send message
Edit message
Manage roles
Manage channels

These classes must remain generic.

They must not contain GameMate-specific or Moderation-specific rules.

6. Shared Command System
   bot/shared/commands/
   ├── Command.java
   ├── CommandContext.java
   ├── CommandHandler.java
   ├── CommandRegistration.java
   └── CommandMetadata.java

This layer defines how commands work inside the platform.

Example:

Discord Interaction
│
▼
CommandRouter
│
▼
CommandHandler
│
▼
Service Command

The shared command system defines the mechanism.

The individual service defines what the command actually does.

7. Shared Event System
   bot/shared/events/
   ├── BotEvent.java
   ├── EventHandler.java
   ├── EventContext.java
   ├── EventRegistration.java
   └── EventDispatcher.java

This layer provides generic event handling.

It does not contain service-specific event processing.

For example:

Discord MEMBER_JOIN
│
▼
Core Bot
│
▼
Shared Event System
│
├──────────────► Moderation
│
├──────────────► Statistics
│
└──────────────► Other Services
8. Shared Permissions
   bot/shared/permissions/
   ├── Permission.java
   ├── PermissionChecker.java
   ├── PermissionContext.java
   └── PermissionResolver.java

The shared layer can provide generic permission mechanisms.

It must not contain service-specific authorization rules.

For example:

Allowed:
Check whether a Discord member has MANAGE_MESSAGES.


Not allowed:
"A user may delete GameMate events only when ..."

The second rule belongs to the relevant service.

9. Shared Localization
   bot/shared/localization/
   ├── LocalizationService.java
   ├── LocaleResolver.java
   └── MessageSource.java

Shared localization infrastructure belongs here.

Service-specific translations remain inside the service:

services/gamemate/
└── src/main/resources/
└── i18n/
├── messages_en.yml
└── messages_de.yml

This keeps the localization infrastructure shared while keeping the actual service content isolated.

10. Contracts

Contracts are the communication boundary between components.

contracts/
├── events/
└── commands/

Contracts must contain definitions only.

They must not contain:

Business logic
Database entities
JPA repositories
Discord-specific implementation
Service implementations
Spring services
Service configuration
11. Event Contracts
    contracts/events/
    └── src/
    └── main/
    └── ...
    └── events/
    ├── member/
    ├── guild/
    ├── command/
    └── service/

Example:

discord.member.joined.v1
discord.member.left.v1
discord.guild.created.v1
discord.command.executed.v1

A contract describes the message structure.

It does not describe how the message is processed.

12. Command Contracts
    contracts/commands/
    └── src/
    └── main/
    └── ...
    └── commands/
    ├── gamemate/
    ├── moderation/
    ├── music/
    └── platform/

Commands describe communication between components.

For example:

discord.command.execute.v1

The command contract can contain:

command ID
guild ID
user ID
command name
arguments
metadata
correlation ID

The actual command implementation belongs to the responsible component.

13. Services

Every service is a completely separate implementation.

services/
├── gamemate/
├── moderation/
├── music/
└── statistics/

A service is responsible for its own:

Business logic
Domain model
Database
Repositories
REST endpoints
Message consumers
Message producers
Configuration
Tests
Service-specific Discord functionality
14. Service Internal Structure

Example:

services/gamemate/
├── build.gradle
│
└── src/
├── main/
│   ├── java/
│   │   └── discord/
│   │       └── platform/
│   │           └── gamemate/
│   │               │
│   │               ├── GameMateApplication.java
│   │               │
│   │               ├── api/
│   │               │   ├── controller/
│   │               │   └── dto/
│   │               │
│   │               ├── application/
│   │               │   ├── command/
│   │               │   ├── query/
│   │               │   └── service/
│   │               │
│   │               ├── domain/
│   │               │   ├── entity/
│   │               │   ├── valueobject/
│   │               │   ├── repository/
│   │               │   └── service/
│   │               │
│   │               ├── infrastructure/
│   │               │   ├── persistence/
│   │               │   ├── external/
│   │               │   └── configuration/
│   │               │
│   │               ├── messaging/
│   │               │   ├── consumer/
│   │               │   └── producer/
│   │               │
│   │               └── discord/
│   │                   ├── command/
│   │                   ├── event/
│   │                   └── adapter/
│   │
│   └── resources/
│       ├── application.yml
│       └── i18n/
│
└── test/
└── java/

The exact package structure may evolve, but the separation of responsibilities must remain.

15. Service Discord Integration

A service may contain Discord-specific adapters.

For example:

services/gamemate/
└── discord/
├── command/
├── event/
└── adapter/

This does not mean that the service owns the Discord connection.

The Core Bot owns JDA.

The service only defines what it needs to expose through Discord.

Conceptually:

                 ┌─────────────────────┐
                 │      Core Bot       │
                 │                     │
Discord ────────►│ JDA / Gateway       │
│ Routing              │
└──────────┬──────────┘
│
▼
┌─────────────────────┐
│ Shared Bot Layer    │
│                     │
│ Commands / Events   │
│ Discord utilities   │
└──────────┬──────────┘
│
┌─────────────┼─────────────┐
▼             ▼             ▼
┌──────────┐  ┌────────────┐  ┌────────┐
│ GameMate │  │ Moderation │  │ Music  │
└──────────┘  └────────────┘  └────────┘

The important distinction is:

Core Bot
owns Discord connection


Shared Bot
owns reusable Discord infrastructure


Service
owns service-specific Discord behavior
16. Strict Dependency Direction

The dependency direction should be:

                    Contracts
                       ▲
                       │
             ┌─────────┴─────────┐
             │                   │
         Core Bot           Services
             │                   │
             ▼                   ▼
        Shared Bot          Infrastructure

More concretely:

Core Bot
├── Shared Bot
└── Contracts


Shared Bot
└── Contracts


Service
└── Contracts


Infrastructure
└── Service/runtime dependencies

A service must never depend on another service.

Forbidden:


gamemate
└── moderation


moderation
└── music


music
└── gamemate
17. No Shared Domain Classes

The following are explicitly forbidden as shared classes:

Game
Guild
User
ModerationCase
MusicSession
Statistics
Event

if these represent actual domain concepts of a service.

For example:

gamemate.domain.Game

belongs exclusively to GameMate.

Statistics must not import it.

If Statistics needs information about a GameMate event, it receives a contract:

gamemate.game.created.v1

and creates its own internal representation.

For example:

GameMate:


Game
├── id
├── name
└── status




Statistics:


GameStatistic
├── gameId
├── duration
└── playerCount

The two models are intentionally independent.

18. Database Isolation

Every service owns its persistence.

Preferred structure:

PostgreSQL
├── gamemate
├── moderation
├── music
└── statistics

A service must never directly access another service's database tables.

Forbidden:

gamemate
└── SELECT * FROM moderation_cases

Communication must instead happen through:

REST

or:

RabbitMQ

depending on the use case.

19. Messaging

RabbitMQ is used as the asynchronous communication layer.

Service A
│
│ Event
▼
RabbitMQ
│
├──────────────► Service B
│
├──────────────► Service C
│
└──────────────► Service D

Services publish and consume contracts.

They do not publish their internal Java classes.

20. Core Bot vs Services

The distinction should remain extremely clear.

Responsibility	Core Bot	Shared Bot	Service
JDA initialization	✓		
Discord connection	✓		
Bot lifecycle	✓		
Command routing	✓	✓
Generic Discord utilities		✓
Generic permissions		✓
Localization infrastructure		✓
Service command implementation			✓
Service business logic			✓
Service database			✓
Service domain model			✓
Service-specific Discord adapter			✓
RabbitMQ integration			✓
Event contracts			Contracts
Command contracts			Contracts
21. Gradle Project Structure

settings.gradle:

rootProject.name = 'discord-platform'


include ':bot:core'
include ':bot:shared'


include ':contracts:events'
include ':contracts:commands'


include ':services:gamemate'
include ':services:moderation'
include ':services:music'
include ':services:statistics'

Infrastructure is intentionally not required to be a Gradle project.

It contains runtime infrastructure such as Docker Compose.

22. Gradle Dependencies

A typical dependency graph should look like:

bot:core
├── bot:shared
└── contracts:events
└── contracts:commands




bot:shared
└── contracts:events
└── contracts:commands




services:gamemate
└── contracts:events
└── contracts:commands




services:moderation
└── contracts:events
└── contracts:commands

There must be no:

services:gamemate
└── services:moderation

and no:

bot:shared
└── services:gamemate
23. Versioning

Services have independent versions.

gamemate: 1.4.0
moderation: 2.1.3
music: 0.8.0
statistics: 1.0.0

Core Bot can have its own version:

bot-core: 1.0.0

Contracts are versioned independently.

Example:

discord.member.joined.v1
discord.member.joined.v2
discord.gamemate.game.created.v1
discord.moderation.case.created.v1

Breaking changes require a new contract version.

24. Naming

Use stable technical identifiers:

core
shared
gamemate
moderation
music
statistics

Do not use display names as routing identifiers.

Technical IDs are used for:

Service names
Routing keys
Metrics
Logging
Configuration
Docker services
Internal identifiers

Display names can change independently.

25. Final Architecture

The resulting architecture is:

discord-platform/
│
├── bot/
│   ├── core/                 ← Discord runtime
│   └── shared/               ← reusable Bot infrastructure
│
├── contracts/
│   ├── events/               ← event definitions
│   └── commands/             ← command definitions
│
├── services/
│   ├── gamemate/             ← GameMate implementation
│   ├── moderation/           ← Moderation implementation
│   ├── music/                ← Music implementation
│   └── statistics/           ← Statistics implementation
│
├── infrastructure/           ← runtime infrastructure
│
└── docs/

The conceptual architecture is:

                         DISCORD
                            │
                            ▼
                  ┌──────────────────┐
                  │     BOT CORE     │
                  │                  │
                  │ JDA              │
                  │ Gateway          │
                  │ Lifecycle        │
                  │ Routing          │
                  └────────┬─────────┘
                           │
                           ▼
                  ┌──────────────────┐
                  │   BOT SHARED     │
                  │                  │
                  │ Commands         │
                  │ Events           │
                  │ Permissions      │
                  │ Discord Utils    │
                  │ Localization     │
                  └────────┬─────────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
              ▼            ▼            ▼
         ┌─────────┐ ┌────────────┐ ┌────────┐
         │ GameMate│ │ Moderation │ │ Music  │
         │ Service │ │  Service   │ │Service │
         └────┬────┘ └─────┬──────┘ └───┬────┘
              │            │             │
              └────────────┼─────────────┘
                           │
                           ▼
                      ┌──────────┐
                      │ RabbitMQ │
                      └────┬─────┘
                           │
                           ▼
                      ┌──────────┐
                      │PostgreSQL│
                      └──────────┘


        ┌──────────────────────────────────────┐
        │              CONTRACTS               │
        │                                      │
        │ Events / Commands / Schemas          │
        │                                      │
        │ Used as communication boundaries     │
        └──────────────────────────────────────┘
Core architectural rule

The most important rule for the repository is:

The Bot is the platform. Services are applications running on that platform.