package com.z3rp436.discord.bot.main.config;

import com.z3rp436.discord.core.BotProfile;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
public record BotMetadataProperties(
        String id,
        String displayName,
        BotProfile defaultProfile
) {
}

