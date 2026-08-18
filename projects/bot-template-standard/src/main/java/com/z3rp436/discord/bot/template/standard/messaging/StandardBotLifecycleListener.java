package com.z3rp436.discord.bot.template.standard.messaging;

import com.z3rp436.discord.bot.template.standard.config.BotMetadataProperties;
import com.z3rp436.discord.bot.template.standard.config.RabbitTopologyConfig;
import com.z3rp436.discord.contracts.BotLifecycleCommand;
import com.z3rp436.discord.contracts.BotStatusEvent;
import com.z3rp436.discord.contracts.ContractSchema;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class StandardBotLifecycleListener {

    private final BotMetadataProperties metadata;
    private final RabbitTemplate rabbitTemplate;

    public StandardBotLifecycleListener(BotMetadataProperties metadata, RabbitTemplate rabbitTemplate) {
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


