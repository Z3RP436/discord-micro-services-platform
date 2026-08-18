package com.z3rp436.discord.bot.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BotMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotMainApplication.class, args);
    }
}

