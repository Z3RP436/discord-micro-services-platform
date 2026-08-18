package com.z3rp436.discord.bot.api.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopologyConfig {

    public static final String BOT_LIFECYCLE_QUEUE = "bot.lifecycle.commands";
    public static final String BOT_STATUS_QUEUE = "bot.status.events";

    @Bean
    Queue botLifecycleQueue() {
        return new Queue(BOT_LIFECYCLE_QUEUE, true);
    }

    @Bean
    Queue botStatusQueue() {
        return new Queue(BOT_STATUS_QUEUE, true);
    }
}
