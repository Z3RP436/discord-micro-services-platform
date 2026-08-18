package com.z3rp436.discord.bot.api.catalog;

import java.util.Set;

public record CatalogBotDefinition(
        String botId,
        String displayName,
        String defaultProfile,
        Set<String> capabilities
) {
}

