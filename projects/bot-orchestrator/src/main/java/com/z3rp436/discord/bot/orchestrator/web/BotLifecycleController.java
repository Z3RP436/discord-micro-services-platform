package com.z3rp436.discord.bot.orchestrator.web;

import com.z3rp436.discord.bot.orchestrator.config.RabbitTopologyConfig;
import com.z3rp436.discord.bot.orchestrator.service.BotStartGuardClient;
import com.z3rp436.discord.bot.orchestrator.web.api.OrchestratorLifecycleApi;
import com.z3rp436.discord.contracts.BotLifecycleAction;
import com.z3rp436.discord.contracts.BotLifecycleCommand;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/orchestrator/bots")
public class BotLifecycleController implements OrchestratorLifecycleApi {

    private final RabbitTemplate rabbitTemplate;
    private final BotStartGuardClient guardClient;

    public BotLifecycleController(RabbitTemplate rabbitTemplate, BotStartGuardClient guardClient) {
        this.rabbitTemplate = rabbitTemplate;
        this.guardClient = guardClient;
    }

    @Override
    public Map<String, String> lifecycle(@PathVariable String botId, @Valid @RequestBody LifecycleRequest request) {
        if (BotLifecycleAction.START.name().equals(request.action())) {
            BotStartGuardClient.GuardResult guard = guardClient.evaluate(botId);
            if (!guard.allowed()) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, guard.reasonCode());
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
}



