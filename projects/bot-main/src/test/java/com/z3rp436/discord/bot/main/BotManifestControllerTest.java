package com.z3rp436.discord.bot.main;

import com.z3rp436.discord.bot.main.config.BotMetadataProperties;
import com.z3rp436.discord.bot.main.plugin.ConfigPlugin;
import com.z3rp436.discord.bot.main.plugin.LoggingPlugin;
import com.z3rp436.discord.bot.main.plugin.ModerationPlugin;
import com.z3rp436.discord.bot.main.service.MainBotPluginRegistry;
import com.z3rp436.discord.bot.main.web.BotManifestController;
import com.z3rp436.discord.core.BotPlugin;
import com.z3rp436.discord.core.BotProfile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BotManifestControllerTest {

    @Test
    void shouldExposeManifestFromMetadataAndRegisteredPlugins() {
        List<BotPlugin> plugins = List.of(new ModerationPlugin(), new LoggingPlugin(), new ConfigPlugin());
        MainBotPluginRegistry registry = new MainBotPluginRegistry(plugins);
        BotMetadataProperties metadata = new BotMetadataProperties("bot-main", "Standard", BotProfile.MEDIUM);

        BotManifestController controller = new BotManifestController(metadata, registry);

        assertThat(controller.manifest().botId()).isEqualTo("bot-main");
        assertThat(controller.manifest().capabilities()).contains("moderation", "logging", "config");
    }
}


