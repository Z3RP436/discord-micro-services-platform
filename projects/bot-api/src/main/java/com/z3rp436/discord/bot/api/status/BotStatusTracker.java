package com.z3rp436.discord.bot.api.status;

import com.z3rp436.discord.bot.api.config.RabbitTopologyConfig;
import com.z3rp436.discord.contracts.BotStatusEvent;
import com.z3rp436.discord.contracts.ContractSchema;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BotStatusTracker {

    private final Map<String, BotStatusEvent> latestStatusByBot = new ConcurrentHashMap<>();

    @RabbitListener(queues = RabbitTopologyConfig.BOT_STATUS_QUEUE)
    public void onStatus(BotStatusEvent event) {
        if (!ContractSchema.isSupported(event.schemaVersion())) {
            return;
        }
        latestStatusByBot.put(event.botId(), event);
    }

    public Optional<BotStatusEvent> latestFor(String botId) {
        return Optional.ofNullable(latestStatusByBot.get(botId));
    }
}


