package com.z3rp436.discord.bot.orchestrator.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopologyConfig {

    public static final String BOT_LIFECYCLE_QUEUE = "bot.lifecycle.commands";

    @Bean
    Queue botLifecycleQueue() {
        return new Queue(BOT_LIFECYCLE_QUEUE, true);
    }
}

