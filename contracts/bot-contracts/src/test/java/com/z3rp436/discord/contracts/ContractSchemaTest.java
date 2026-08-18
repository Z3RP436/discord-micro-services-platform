package com.z3rp436.discord.contracts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContractSchemaTest {

    @Test
    void shouldCreateV1LifecycleCommandWithRequiredMetadata() {
        BotLifecycleCommand command = BotLifecycleCommand.v1("bot-a", BotLifecycleAction.START, "MEDIUM", "api-user");

        assertEquals(ContractSchema.V1, command.schemaVersion());
        assertEquals(ContractMessageType.BOT_LIFECYCLE_COMMAND_V1, command.messageType());
        assertEquals("bot-a", command.botId());
        assertEquals(BotLifecycleAction.START, command.action());
        assertFalse(command.eventId().isBlank());
        assertFalse(command.correlationId().isBlank());
    }

    @Test
    void shouldCreateV1StatusEventAndPreserveCorrelationId() {
        BotStatusEvent event = BotStatusEvent.v1("corr-123", "bot-a", "START", "MEDIUM");

        assertEquals(ContractSchema.V1, event.schemaVersion());
        assertEquals(ContractMessageType.BOT_STATUS_EVENT_V1, event.messageType());
        assertEquals("corr-123", event.correlationId());
        assertEquals("bot-a", event.botId());
        assertFalse(event.eventId().isBlank());
    }

    @Test
    void shouldMarkUnsupportedSchemaAsUnsupported() {
        assertTrue(ContractSchema.isSupported("v1"));
        assertFalse(ContractSchema.isSupported("v2"));
    }
}

