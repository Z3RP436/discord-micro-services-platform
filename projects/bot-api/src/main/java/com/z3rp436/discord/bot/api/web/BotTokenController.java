package com.z3rp436.discord.bot.api.web;

import com.z3rp436.discord.bot.api.service.BotDirectoryService;
import com.z3rp436.discord.bot.api.service.BotTokenService;
import com.z3rp436.discord.bot.api.web.api.BotTokenApi;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/bots")
public class BotTokenController implements BotTokenApi {

    private final BotDirectoryService botDirectoryService;
    private final BotTokenService botTokenService;

    public BotTokenController(BotDirectoryService botDirectoryService, BotTokenService botTokenService) {
        this.botDirectoryService = botDirectoryService;
        this.botTokenService = botTokenService;
    }

    @Override
    public TokenStatusResponse upsertToken(@PathVariable String botId, @Valid @RequestBody UpsertTokenRequest request) {
        assertRunnable(botId);
        String updatedBy = request.updatedBy() == null || request.updatedBy().isBlank() ? "unknown" : request.updatedBy();
        BotTokenService.TokenStatusView status = botTokenService.upsert(botId, request.jdaToken(), updatedBy);
        return map(status);
    }

    @Override
    public TokenStatusResponse tokenStatus(@PathVariable String botId) {
        assertRunnable(botId);
        return botTokenService.getStatus(botId)
                .map(this::map)
                .orElse(new TokenStatusResponse(botId, false, "REVOKED", null, null));
    }

    @Override
    public TokenStatusResponse deleteToken(@PathVariable String botId) {
        assertRunnable(botId);
        try {
            return map(botTokenService.delete(botId, "control-panel"));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, ex.getMessage());
        }
    }

    private void assertRunnable(String botId) {
        if (!botDirectoryService.existsBot(botId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bot not found: " + botId);
        }
        if (!botDirectoryService.isRunnableBot(botId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bot is not runnable: " + botId);
        }
    }

    private TokenStatusResponse map(BotTokenService.TokenStatusView view) {
        return new TokenStatusResponse(
                view.botId(),
                view.tokenConfigured(),
                view.tokenStatus(),
                view.tokenFingerprint(),
                view.updatedAt()
        );
    }
}


