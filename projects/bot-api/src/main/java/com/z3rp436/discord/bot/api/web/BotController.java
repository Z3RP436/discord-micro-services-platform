package com.z3rp436.discord.bot.api.web;

import com.z3rp436.discord.bot.api.config.RabbitTopologyConfig;
import com.z3rp436.discord.bot.api.service.BotDirectoryService;
import com.z3rp436.discord.contracts.BotLifecycleAction;
import com.z3rp436.discord.contracts.BotLifecycleCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    private final BotDirectoryService botDirectoryService;
    private final RabbitTemplate rabbitTemplate;

    public BotController(BotDirectoryService botDirectoryService, RabbitTemplate rabbitTemplate) {
        this.botDirectoryService = botDirectoryService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public List<BotDirectoryService.BotView> allBots() {
        return botDirectoryService.all();
    }

    @PutMapping("/{botId}/guilds/{guildId}/profile")
    public Map<String, String> setProfileOverride(
            @PathVariable String botId,
            @PathVariable String guildId,
            @Valid @RequestBody SetProfileRequest request
    ) {
        botDirectoryService.setProfileOverride(botId, guildId, request.profile());
        return Map.of("status", "saved", "botId", botId, "guildId", guildId, "profile", request.profile());
    }

    @PostMapping("/{botId}/lifecycle")
    public Map<String, String> lifecycle(@PathVariable String botId, @Valid @RequestBody LifecycleRequest request) {
        BotLifecycleCommand command = BotLifecycleCommand.v1(
                botId,
                BotLifecycleAction.valueOf(request.action()),
                request.profile(),
                request.requestedBy()
        );

        rabbitTemplate.convertAndSend(RabbitTopologyConfig.BOT_LIFECYCLE_QUEUE, command);

        return Map.of("status", "accepted", "botId", botId, "action", request.action());
    }

    public record SetProfileRequest(@NotBlank String profile) {
    }

    public record LifecycleRequest(
            @NotBlank String action,
            @NotBlank String profile,
            @NotBlank String requestedBy
    ) {
    }
}



