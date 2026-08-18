package com.z3rp436.discord.bot.api.web;

import com.z3rp436.discord.bot.api.config.RabbitTopologyConfig;
import com.z3rp436.discord.bot.api.service.BotDirectoryService;
import com.z3rp436.discord.bot.api.service.BotTokenService;
import com.z3rp436.discord.bot.api.web.api.BotLifecycleApi;
import com.z3rp436.discord.bot.api.web.api.BotManagementApi;
import com.z3rp436.discord.bot.api.web.api.BotTokenApi;
import com.z3rp436.discord.contracts.BotLifecycleAction;
import com.z3rp436.discord.contracts.BotLifecycleCommand;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bots")
public class BotController implements BotManagementApi {

    private final BotDirectoryService botDirectoryService;
    private final BotTokenService botTokenService;
    private final RabbitTemplate rabbitTemplate;

    public BotController(
            BotDirectoryService botDirectoryService,
            BotTokenService botTokenService,
            RabbitTemplate rabbitTemplate
    ) {
        this.botDirectoryService = botDirectoryService;
        this.botTokenService = botTokenService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public List<BotDirectoryService.BotView> allBots() {
        return botDirectoryService.all();
    }

    @Override
    public Map<String, String> setProfileOverride(
            @PathVariable String botId,
            @PathVariable String guildId,
            @Valid @RequestBody BotManagementApi.SetProfileRequest request
    ) {
        botDirectoryService.setProfileOverride(botId, guildId, request.profile());
        return Map.of("status", "saved", "botId", botId, "guildId", guildId, "profile", request.profile());
    }

    @Override
    public Map<String, String> lifecycle(@PathVariable String botId, @Valid @RequestBody BotLifecycleApi.LifecycleRequest request) {
        if (!botDirectoryService.existsBot(botId)) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Bot not found: " + botId);
        }
        if (!botDirectoryService.isRunnableBot(botId)) {
            throw new BotApiException(
                    HttpStatus.CONFLICT,
                    new BotTokenApi.ErrorResponse(
                            "BOT_NOT_RUNNABLE",
                            "Bot is not runnable.",
                            botId,
                            request.action(),
                            Instant.now().toString()
                    ).toMap()
            );
        }

        if (BotLifecycleAction.START.name().equals(request.action())) {
            BotTokenService.GuardResult guard = botTokenService.evaluateStartGuard(botId);
            if (!guard.allowed()) {
                Map<String, Object> errorBody = new BotTokenApi.ErrorResponse(
                        guard.reasonCode(),
                        "Bot start requires an active JDA token.",
                        botId,
                        "START",
                        Instant.now().toString()
                ).toMap();

                rabbitTemplate.convertAndSend(
                        RabbitTopologyConfig.BOT_AUDIT_QUEUE,
                        Map.of(
                                "eventType", "bot.start.blocked",
                                "reasonCode", guard.reasonCode(),
                                "botId", botId,
                                "timestamp", Instant.now().toString()
                        )
                );

                throw new BotApiException(HttpStatus.PRECONDITION_FAILED, errorBody);
            }
        }

        BotLifecycleCommand command = BotLifecycleCommand.v1(
                botId,
                BotLifecycleAction.valueOf(request.action()),
                request.profile(),
                request.requestedBy()
        );

        rabbitTemplate.convertAndSend(RabbitTopologyConfig.BOT_LIFECYCLE_QUEUE, command);

        return Map.of("status", "accepted", "botId", botId, "action", request.action());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BotApiException.class)
    public ResponseEntity<Map<String, Object>> handleGuardException(BotApiException ex) {
        return ResponseEntity.status(ex.status).body(ex.body);
    }

    public static class BotApiException extends RuntimeException {
        private final HttpStatus status;
        private final Map<String, Object> body;

        BotApiException(HttpStatus status, Map<String, Object> body) {
            this.status = status;
            this.body = body;
        }
    }
}



