package com.z3rp436.discord.bot.api.service;

import com.z3rp436.discord.bot.api.catalog.BotCatalogLoader;
import com.z3rp436.discord.bot.api.catalog.CatalogBotDefinition;
import com.z3rp436.discord.bot.api.persistence.BotProfileOverrideEntity;
import com.z3rp436.discord.bot.api.persistence.BotProfileOverrideRepository;
import com.z3rp436.discord.bot.api.status.BotStatusTracker;
import com.z3rp436.discord.contracts.BotStatusEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BotDirectoryService {

    private final BotCatalogLoader catalogLoader;
    private final BotStatusTracker statusTracker;
    private final BotProfileOverrideRepository overrideRepository;

    public BotDirectoryService(
            BotCatalogLoader catalogLoader,
            BotStatusTracker statusTracker,
            BotProfileOverrideRepository overrideRepository
    ) {
        this.catalogLoader = catalogLoader;
        this.statusTracker = statusTracker;
        this.overrideRepository = overrideRepository;
    }

    public List<BotView> all() {
        return catalogLoader.loadCatalog().stream()
                .map(this::toView)
                .toList();
    }

    public void setProfileOverride(String botId, String guildId, String profile) {
        BotProfileOverrideEntity entity = overrideRepository.findByBotIdAndGuildId(botId, guildId)
                .orElse(new BotProfileOverrideEntity(botId, guildId, profile));
        entity.setProfile(profile);
        overrideRepository.save(entity);
    }

    private BotView toView(CatalogBotDefinition entry) {
        BotStatusEvent status = statusTracker.latestFor(entry.botId()).orElse(null);

        return new BotView(
                entry.botId(),
                entry.displayName(),
                entry.defaultProfile(),
                entry.capabilities(),
                status == null ? "UNKNOWN" : status.status(),
                status == null ? null : status.timestamp().toString()
        );
    }

    public record BotView(
            String botId,
            String displayName,
            String defaultProfile,
            java.util.Set<String> capabilities,
            String liveStatus,
            String lastStatusTimestamp
    ) {
    }
}

