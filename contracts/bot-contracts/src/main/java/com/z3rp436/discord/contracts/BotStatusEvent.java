package com.z3rp436.discord.contracts;

import java.time.Instant;
import java.util.UUID;

public record BotStatusEvent(
        String schemaVersion,
        String messageType,
        String eventId,
        String correlationId,
        String botId,
        String status,
        String profile,
        Instant timestamp
) {

    public BotStatusEvent {
        requireText(schemaVersion, "schemaVersion");
        requireText(messageType, "messageType");
        requireText(eventId, "eventId");
        requireText(correlationId, "correlationId");
        requireText(botId, "botId");
        requireText(status, "status");
        requireText(profile, "profile");
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp must not be null");
        }
        if (!ContractSchema.isSupported(schemaVersion)) {
            throw new IllegalArgumentException("Unsupported schema version: " + schemaVersion);
        }
    }

    public static BotStatusEvent v1(String correlationId, String botId, String status, String profile) {
        return new BotStatusEvent(
                ContractSchema.V1,
                ContractMessageType.BOT_STATUS_EVENT_V1,
                UUID.randomUUID().toString(),
                correlationId,
                botId,
                status,
                profile,
                Instant.now()
        );
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}

