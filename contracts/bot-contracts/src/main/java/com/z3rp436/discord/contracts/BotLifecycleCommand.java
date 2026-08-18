package com.z3rp436.discord.contracts;

import java.time.Instant;
import java.util.UUID;

public record BotLifecycleCommand(
        String schemaVersion,
        String messageType,
        String eventId,
        Instant timestamp,
        String correlationId,
        String botId,
        BotLifecycleAction action,
        String profile,
        String requestedBy
) {

    public BotLifecycleCommand {
        requireText(schemaVersion, "schemaVersion");
        requireText(messageType, "messageType");
        requireText(eventId, "eventId");
        requireText(correlationId, "correlationId");
        requireText(botId, "botId");
        requireText(profile, "profile");
        requireText(requestedBy, "requestedBy");

        if (!ContractSchema.isSupported(schemaVersion)) {
            throw new IllegalArgumentException("Unsupported schema version: " + schemaVersion);
        }
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp must not be null");
        }
    }

    public static BotLifecycleCommand v1(
            String botId,
            BotLifecycleAction action,
            String profile,
            String requestedBy
    ) {
        return new BotLifecycleCommand(
                ContractSchema.V1,
                ContractMessageType.BOT_LIFECYCLE_COMMAND_V1,
                UUID.randomUUID().toString(),
                Instant.now(),
                UUID.randomUUID().toString(),
                botId,
                action,
                profile,
                requestedBy
        );
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}

