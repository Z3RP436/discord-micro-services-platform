package com.z3rp436.discord.bot.api.service;

import com.z3rp436.discord.bot.api.persistence.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class BotTokenService {

    private final BotTokenActiveRepository activeRepository;
    private final BotTokenHistoryRepository historyRepository;

    public BotTokenService(
            BotTokenActiveRepository activeRepository,
            BotTokenHistoryRepository historyRepository
    ) {
        this.activeRepository = activeRepository;
        this.historyRepository = historyRepository;
    }

    public TokenStatusView upsert(String botId, String rawToken, String updatedBy) {
        String fingerprint = fingerprint(rawToken);

        BotTokenActiveEntity entity = activeRepository.findByBotId(botId)
                .map(existing -> {
                    historyRepository.save(new BotTokenHistoryEntity(
                            existing.botId(),
                            existing.tokenFingerprint(),
                            BotTokenHistoryStatus.REPLACED,
                            updatedBy,
                            "token updated"
                    ));
                    existing.replaceToken(rawToken, fingerprint, updatedBy);
                    return existing;
                })
                .orElse(new BotTokenActiveEntity(botId, rawToken, fingerprint, updatedBy));

        activeRepository.save(entity);
        return toView(entity);
    }

    public Optional<TokenStatusView> getStatus(String botId) {
        return activeRepository.findByBotId(botId).map(this::toView);
    }

    public TokenStatusView delete(String botId, String deletedBy) {
        BotTokenActiveEntity existing = activeRepository.findByBotId(botId)
                .orElseThrow(() -> new IllegalArgumentException("BOT_TOKEN_MISSING"));

        historyRepository.save(new BotTokenHistoryEntity(
                existing.botId(),
                existing.tokenFingerprint(),
                BotTokenHistoryStatus.DELETED,
                deletedBy,
                "token deleted"
        ));

        activeRepository.deleteByBotId(botId);
        return new TokenStatusView(botId, false, BotTokenStatus.REVOKED.name(), existing.tokenFingerprint(), Instant.now().toString());
    }

    public GuardResult evaluateStartGuard(String botId) {
        Optional<BotTokenActiveEntity> maybeToken = activeRepository.findByBotId(botId);
        if (maybeToken.isEmpty()) {
            return new GuardResult(false, "TOKEN_MISSING");
        }

        BotTokenActiveEntity token = maybeToken.get();
        token.markStartAttempt();
        token.setLastValidatedAt(Instant.now());
        activeRepository.save(token);

        if (token.tokenStatus() != BotTokenStatus.ACTIVE) {
            return new GuardResult(false, "TOKEN_INVALID");
        }

        return new GuardResult(true, null);
    }

    private TokenStatusView toView(BotTokenActiveEntity entity) {
        return new TokenStatusView(
                entity.botId(),
                true,
                entity.tokenStatus().name(),
                entity.tokenFingerprint(),
                entity.updatedAt().toString()
        );
    }

    private String fingerprint(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return "sha256:" + Base64.getUrlEncoder().withoutPadding().encodeToString(hash).substring(0, 18);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not generate token fingerprint", ex);
        }
    }

    public record TokenStatusView(
            String botId,
            boolean tokenConfigured,
            String tokenStatus,
            String tokenFingerprint,
            String updatedAt
    ) {
    }

    public record GuardResult(boolean allowed, String reasonCode) {
    }
}



