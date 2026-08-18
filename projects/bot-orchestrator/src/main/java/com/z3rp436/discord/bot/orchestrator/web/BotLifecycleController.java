package com.z3rp436.discord.bot.orchestrator.web;

import com.z3rp436.discord.bot.orchestrator.config.RabbitTopologyConfig;
import com.z3rp436.discord.contracts.BotLifecycleAction;
import com.z3rp436.discord.contracts.BotLifecycleCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orchestrator/bots")
public class BotLifecycleController {

    private final RabbitTemplate rabbitTemplate;

    public BotLifecycleController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
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

    public record LifecycleRequest(
            @NotBlank String action,
            @NotBlank String profile,
            @NotBlank String requestedBy
    ) {
    }
}



