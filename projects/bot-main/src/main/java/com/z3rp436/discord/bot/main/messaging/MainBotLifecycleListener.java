package com.z3rp436.discord.bot.main.messaging;

import com.z3rp436.discord.bot.main.config.BotMetadataProperties;
import com.z3rp436.discord.bot.main.config.RabbitTopologyConfig;
import com.z3rp436.discord.contracts.BotLifecycleCommand;
import com.z3rp436.discord.contracts.BotStatusEvent;
import com.z3rp436.discord.contracts.ContractSchema;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MainBotLifecycleListener {

    private final BotMetadataProperties metadata;
    private final RabbitTemplate rabbitTemplate;

    public MainBotLifecycleListener(BotMetadataProperties metadata, RabbitTemplate rabbitTemplate) {
        this.metadata = metadata;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitTopologyConfig.BOT_LIFECYCLE_QUEUE)
    public void onLifecycleCommand(BotLifecycleCommand command) {
        if (!ContractSchema.isSupported(command.schemaVersion())) {
            return;
        }

        if (!metadata.id().equals(command.botId())) {
            return;
        }

        rabbitTemplate.convertAndSend(
                RabbitTopologyConfig.BOT_STATUS_QUEUE,
                BotStatusEvent.v1(command.correlationId(), metadata.id(), command.action().name(), command.profile())
        );
    }
}


