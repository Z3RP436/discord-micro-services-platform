package com.z3rp436.discord.core;

import java.util.Map;

public record BotCommandContext(
        String guildId,
        String userId,
        String command,
        Map<String, String> arguments
) {
}

