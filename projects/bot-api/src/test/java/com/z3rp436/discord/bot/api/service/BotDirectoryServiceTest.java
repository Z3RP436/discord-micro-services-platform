package com.z3rp436.discord.bot.api.service;

import com.z3rp436.discord.bot.api.catalog.BotCatalogLoader;
import com.z3rp436.discord.bot.api.catalog.CatalogBotDefinition;
import com.z3rp436.discord.bot.api.catalog.ServiceType;
import com.z3rp436.discord.bot.api.persistence.BotProfileOverrideRepository;
import com.z3rp436.discord.bot.api.persistence.BotTokenActiveEntity;
import com.z3rp436.discord.bot.api.persistence.BotTokenActiveRepository;
import com.z3rp436.discord.bot.api.status.BotStatusTracker;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BotDirectoryServiceTest {

    @Test
    void shouldExposeConfiguredAndRunnableFlags() {
        BotCatalogLoader catalogLoader = mock(BotCatalogLoader.class);
        BotStatusTracker statusTracker = mock(BotStatusTracker.class);
        BotProfileOverrideRepository overrideRepository = mock(BotProfileOverrideRepository.class);
        BotTokenActiveRepository tokenActiveRepository = mock(BotTokenActiveRepository.class);

        CatalogBotDefinition botMain = new CatalogBotDefinition(
                "bot-main",
                "Main Bot",
                "MEDIUM",
                Set.of("moderation"),
                ServiceType.RUNNABLE_BOT
        );

        when(catalogLoader.loadCatalog()).thenReturn(List.of(botMain));
        when(statusTracker.latestFor("bot-main")).thenReturn(Optional.empty());
        when(tokenActiveRepository.findByBotId("bot-main"))
                .thenReturn(Optional.of(new BotTokenActiveEntity("bot-main", "plain-token", "fp", "test")));

        BotDirectoryService service = new BotDirectoryService(catalogLoader, statusTracker, overrideRepository, tokenActiveRepository);

        List<BotDirectoryService.BotView> bots = service.all();

        assertThat(bots).hasSize(1);
        assertThat(bots.getFirst().configured()).isTrue();
        assertThat(bots.getFirst().runnable()).isTrue();
        assertThat(bots.getFirst().reasonCode()).isNull();
    }

    @Test
    void shouldMarkBotAsNotRunnableWhenTokenMissing() {
        BotCatalogLoader catalogLoader = mock(BotCatalogLoader.class);
        BotStatusTracker statusTracker = mock(BotStatusTracker.class);
        BotProfileOverrideRepository overrideRepository = mock(BotProfileOverrideRepository.class);
        BotTokenActiveRepository tokenActiveRepository = mock(BotTokenActiveRepository.class);

        CatalogBotDefinition botMain = new CatalogBotDefinition(
                "bot-main",
                "Main Bot",
                "MEDIUM",
                Set.of("moderation"),
                ServiceType.RUNNABLE_BOT
        );

        when(catalogLoader.loadCatalog()).thenReturn(List.of(botMain));
        when(statusTracker.latestFor("bot-main")).thenReturn(Optional.empty());
        when(tokenActiveRepository.findByBotId("bot-main")).thenReturn(Optional.empty());

        BotDirectoryService service = new BotDirectoryService(catalogLoader, statusTracker, overrideRepository, tokenActiveRepository);

        List<BotDirectoryService.BotView> bots = service.all();

        assertThat(bots).hasSize(1);
        assertThat(bots.getFirst().configured()).isFalse();
        assertThat(bots.getFirst().runnable()).isFalse();
        assertThat(bots.getFirst().reasonCode()).isEqualTo("TOKEN_MISSING");
    }
}

