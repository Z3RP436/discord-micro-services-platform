package com.z3rp436.discord.core;

public interface BotPlugin {

    String pluginName();

    String handle(BotCommandContext context);
}

