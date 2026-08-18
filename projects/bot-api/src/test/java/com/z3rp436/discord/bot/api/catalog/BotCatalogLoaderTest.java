package com.z3rp436.discord.bot.api.catalog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class BotCatalogLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldLoadBotsFromCatalogFile() throws IOException {
        Path catalogFile = tempDir.resolve("bots.json");
        Files.writeString(catalogFile, """
                {"bots":[{"botId":"bot-template-standard","displayName":"Standard","defaultProfile":"MEDIUM","capabilities":["moderation","logging"]}]}
                """);

        BotCatalogLoader loader = new BotCatalogLoader(catalogFile.toString());

        assertThat(loader.loadCatalog()).hasSize(1);
        assertThat(loader.loadCatalog().getFirst().botId()).isEqualTo("bot-template-standard");
    }
}

