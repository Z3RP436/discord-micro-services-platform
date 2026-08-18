package com.z3rp436.discord.bot.template.standard;

import com.z3rp436.discord.bot.template.standard.config.BotMetadataProperties;
import com.z3rp436.discord.bot.template.standard.plugin.ConfigPlugin;
import com.z3rp436.discord.bot.template.standard.plugin.LoggingPlugin;
import com.z3rp436.discord.bot.template.standard.plugin.ModerationPlugin;
import com.z3rp436.discord.bot.template.standard.service.StandardBotPluginRegistry;
import com.z3rp436.discord.bot.template.standard.web.BotManifestController;
import com.z3rp436.discord.core.BotPlugin;
import com.z3rp436.discord.core.BotProfile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BotManifestControllerTest {

    @Test
    void shouldExposeManifestFromMetadataAndRegisteredPlugins() {
        List<BotPlugin> plugins = List.of(new ModerationPlugin(), new LoggingPlugin(), new ConfigPlugin());
        StandardBotPluginRegistry registry = new StandardBotPluginRegistry(plugins);
        BotMetadataProperties metadata = new BotMetadataProperties("bot-template-standard", "Standard", BotProfile.MEDIUM);

        BotManifestController controller = new BotManifestController(metadata, registry);

        assertThat(controller.manifest().botId()).isEqualTo("bot-template-standard");
        assertThat(controller.manifest().capabilities()).contains("moderation", "logging", "config");
    }
}


