package com.z3rp436.discord.bot.api.service;

import com.z3rp436.discord.bot.api.catalog.BotCatalogLoader;
import com.z3rp436.discord.bot.api.catalog.CatalogBotDefinition;
import com.z3rp436.discord.bot.api.catalog.ServiceType;
import com.z3rp436.discord.bot.api.persistence.BotTokenActiveRepository;
import com.z3rp436.discord.bot.api.persistence.BotTokenStatus;
import com.z3rp436.discord.bot.api.persistence.BotProfileOverrideEntity;
import com.z3rp436.discord.bot.api.persistence.BotProfileOverrideRepository;
import com.z3rp436.discord.bot.api.status.BotStatusTracker;
import com.z3rp436.discord.contracts.BotStatusEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotDirectoryService {

    private final BotCatalogLoader catalogLoader;
    private final BotStatusTracker statusTracker;
    private final BotProfileOverrideRepository overrideRepository;
    private final BotTokenActiveRepository tokenActiveRepository;

    public BotDirectoryService(
            BotCatalogLoader catalogLoader,
            BotStatusTracker statusTracker,
            BotProfileOverrideRepository overrideRepository,
            BotTokenActiveRepository tokenActiveRepository
    ) {
        this.catalogLoader = catalogLoader;
        this.statusTracker = statusTracker;
        this.overrideRepository = overrideRepository;
        this.tokenActiveRepository = tokenActiveRepository;
    }

    public List<BotView> all() {
        return catalogLoader.loadCatalog().stream()
                .filter(this::isRunnableCatalogService)
                .map(this::toView)
                .toList();
    }

    public boolean isRunnableBot(String botId) {
        return catalogLoader.loadCatalog().stream()
                .anyMatch(entry -> botId.equals(entry.botId()) && isRunnableCatalogService(entry));
    }

    public boolean existsBot(String botId) {
        return catalogLoader.loadCatalog().stream().anyMatch(entry -> botId.equals(entry.botId()));
    }

    public void setProfileOverride(String botId, String guildId, String profile) {
        BotProfileOverrideEntity entity = overrideRepository.findByBotIdAndGuildId(botId, guildId)
                .orElse(new BotProfileOverrideEntity(botId, guildId, profile));
        entity.setProfile(profile);
        overrideRepository.save(entity);
    }

    private BotView toView(CatalogBotDefinition entry) {
        BotStatusEvent status = statusTracker.latestFor(entry.botId()).orElse(null);
        var token = tokenActiveRepository.findByBotId(entry.botId());
        boolean configured = token.isPresent();
        boolean runnable = token.map(active -> active.tokenStatus() == BotTokenStatus.ACTIVE).orElse(false);
        String reasonCode = null;
        if (!runnable) {
            reasonCode = configured ? "TOKEN_INVALID" : "TOKEN_MISSING";
        }

        return new BotView(
                entry.botId(),
                entry.displayName(),
                entry.defaultProfile(),
                entry.capabilities(),
                configured,
                runnable,
                reasonCode,
                status == null ? "UNKNOWN" : status.status(),
                status == null ? null : status.timestamp().toString()
        );
    }

    private boolean isRunnableCatalogService(CatalogBotDefinition entry) {
        return entry.serviceType() == ServiceType.RUNNABLE_BOT;
    }

    public record BotView(
            String botId,
            String displayName,
            String defaultProfile,
            java.util.Set<String> capabilities,
            boolean configured,
            boolean runnable,
            String reasonCode,
            String liveStatus,
            String lastStatusTimestamp
    ) {
    }
}

