package com.z3rp436.discord.bot.api.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class BotCatalogLoader {

    private final ObjectMapper objectMapper;
    private final Path catalogPath;

    public BotCatalogLoader(@Value("${bot.catalog.path}") String catalogPath) {
        this.objectMapper = new ObjectMapper();
        this.catalogPath = Path.of(catalogPath);
    }

    public List<CatalogBotDefinition> loadCatalog() {
        if (!Files.exists(catalogPath)) {
            return List.of();
        }

        try {
            CatalogBotsFile file = objectMapper.readValue(catalogPath.toFile(), CatalogBotsFile.class);
            return file.bots() == null ? List.of() : file.bots();
        } catch (IOException ex) {
            throw new IllegalStateException("Could not read bot catalog at " + catalogPath, ex);
        }
    }
}

