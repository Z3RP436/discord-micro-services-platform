# 07 – GameMate Reference Service

## 1. Role

GameMate is the first reference service and should demonstrate the complete platform flow.

It owns GameMate business logic and data, while the Bot Gateway owns Discord connectivity.

## 2. Target architecture

```text
Discord
  ↓
Bot Gateway / JDA
  ↓
RabbitMQ
  ↓
GameMate
  ↓
GameMate DB

GameMate
  ↓
RabbitMQ
  ↓
Bot Gateway / JDA
  ↓
Discord
```

## 3. Example commands

```text
/event
/event-create
/event-list
/event-cancel
```

## 4. Example event

```text
discord.member.joined
        ↓
GameMate consumer
        ↓
load guild configuration
        ↓
execute domain rule
        ↓
optional DiscordActionRequest
```

## 5. Example Spring consumer

```java
@Component
public class DiscordEventConsumer {

    @RabbitListener(queues = "${messaging.queues.gamemate-events}")
    public void handle(PlatformEvent event) {
        switch (event.type()) {
            case "discord.member.joined" -> handleMemberJoined(event);
            case "discord.member.left" -> handleMemberLeft(event);
            default -> {
                // Ignore events not supported by this service.
            }
        }
    }
}
```

## 6. No JDA dependency

The GameMate service should not have:

```java
import net.dv8tion.jda.api.*;
```

Discord concepts are represented by platform contracts.

This makes GameMate easier to test and allows the same domain logic to be triggered by:

- Discord
- Control Panel
- scheduled jobs
- future REST endpoints

## 7. Data

GameMate owns its own entities.

For example:

```text
events
event_participants
teams
matches
match_settings
```

The exact schema belongs to GameMate and should not leak into the Platform API.

## 8. Migration strategy

If an existing GameMate application currently starts JDA itself:

1. Extract business logic from listeners.
2. Introduce application use cases.
3. Replace JDA event handlers with platform event consumers.
4. Replace JDA calls with Discord action requests.
5. Move Discord credentials to Bot Gateway.
6. Remove JDA dependency from GameMate.
7. Deploy GameMate independently.
