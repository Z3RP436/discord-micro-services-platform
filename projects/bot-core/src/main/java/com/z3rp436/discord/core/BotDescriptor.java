package com.z3rp436.discord.core;

import java.util.Set;

public record BotDescriptor(
        String botId,
        String displayName,
        Set<String> capabilities,
        BotProfile defaultProfile
) {
}

