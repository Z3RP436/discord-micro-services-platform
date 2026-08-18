package com.z3rp436.discord.bot.template.standard.plugin;

import com.z3rp436.discord.core.BotCommandContext;
import com.z3rp436.discord.core.BotPlugin;
import org.springframework.stereotype.Component;

@Component
public class LoggingPlugin implements BotPlugin {

    @Override
    public String pluginName() {
        return "logging";
    }

    @Override
    public String handle(BotCommandContext context) {
        return "logging command accepted: " + context.command();
    }
}

