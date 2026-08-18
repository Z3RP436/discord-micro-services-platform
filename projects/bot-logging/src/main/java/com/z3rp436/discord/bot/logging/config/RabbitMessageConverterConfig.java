package com.z3rp436.discord.bot.logging.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMessageConverterConfig {

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}


