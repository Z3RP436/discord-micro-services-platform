package com.z3rp436.discord.bot.api.web.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface BotTokenApi {

    @PutMapping("/{botId}/token")
    TokenStatusResponse upsertToken(@PathVariable String botId, @Valid @RequestBody UpsertTokenRequest request);

    @GetMapping("/{botId}/token")
    TokenStatusResponse tokenStatus(@PathVariable String botId);

    @DeleteMapping("/{botId}/token")
    TokenStatusResponse deleteToken(@PathVariable String botId);

    record UpsertTokenRequest(
            @NotBlank String jdaToken,
            String updatedBy
    ) {
    }

    record TokenStatusResponse(
            String botId,
            boolean tokenConfigured,
            String tokenStatus,
            String tokenFingerprint,
            String updatedAt
    ) {
    }

    record ErrorResponse(
            String errorCode,
            String message,
            String botId,
            String action,
            String timestamp
    ) {
        public Map<String, Object> toMap() {
            return Map.of(
                    "errorCode", errorCode,
                    "message", message,
                    "botId", botId,
                    "action", action,
                    "timestamp", timestamp
            );
        }
    }
}

