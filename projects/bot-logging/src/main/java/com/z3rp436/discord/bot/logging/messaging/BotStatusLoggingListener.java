package com.z3rp436.discord.bot.logging.messaging;

import com.z3rp436.discord.bot.logging.config.RabbitTopologyConfig;
import com.z3rp436.discord.contracts.BotStatusEvent;
import com.z3rp436.discord.contracts.ContractSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BotStatusLoggingListener {

    private static final Logger log = LoggerFactory.getLogger(BotStatusLoggingListener.class);

    @RabbitListener(queues = RabbitTopologyConfig.BOT_STATUS_QUEUE)
    public void onStatus(BotStatusEvent event) {
        if (!ContractSchema.isSupported(event.schemaVersion())) {
            log.warn("ignore unsupported status schemaVersion={} eventId={}", event.schemaVersion(), event.eventId());
            return;
        }

        log.info(
                "version={} type={} eventId={} correlationId={} bot={} status={} profile={} timestamp={}",
                event.schemaVersion(),
                event.messageType(),
                event.eventId(),
                event.correlationId(),
                event.botId(),
                event.status(),
                event.profile(),
                event.timestamp()
        );
    }
}

