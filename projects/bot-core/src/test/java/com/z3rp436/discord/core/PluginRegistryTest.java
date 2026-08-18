package com.z3rp436.discord.core;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginRegistryTest {

    @Test
    void shouldRegisterAndResolvePluginByName() {
        PluginRegistry registry = new PluginRegistry();
        BotPlugin plugin = new BotPlugin() {
            @Override
            public String pluginName() {
                return "moderation";
            }

            @Override
            public String handle(BotCommandContext context) {
                return "ok";
            }
        };

        registry.register(plugin);

        assertTrue(registry.findByName("moderation").isPresent());
        assertFalse(registry.findByName("missing").isPresent());
        assertEquals("ok", registry.findByName("moderation").orElseThrow()
                .handle(new BotCommandContext("g1", "u1", "ban", Map.of())));
    }
}


