package com.z3rp436.discord.bot.logging.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopologyConfig {

    public static final String BOT_STATUS_QUEUE = "bot.status.events";

    @Bean
    Queue botStatusQueue() {
        return new Queue(BOT_STATUS_QUEUE, true);
    }
}
