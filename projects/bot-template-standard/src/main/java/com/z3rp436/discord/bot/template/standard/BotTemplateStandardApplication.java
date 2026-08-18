package com.z3rp436.discord.bot.template.standard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BotTemplateStandardApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotTemplateStandardApplication.class, args);
    }
}

