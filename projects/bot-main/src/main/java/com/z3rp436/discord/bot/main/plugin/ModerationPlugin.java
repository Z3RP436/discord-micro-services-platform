package com.z3rp436.discord.bot.main.plugin;

import com.z3rp436.discord.core.BotCommandContext;
import com.z3rp436.discord.core.BotPlugin;
import org.springframework.stereotype.Component;

@Component
public class ModerationPlugin implements BotPlugin {

    @Override
    public String pluginName() {
        return "moderation";
    }

    @Override
    public String handle(BotCommandContext context) {
        return "moderation command accepted: " + context.command();
    }
}

