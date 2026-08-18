package com.z3rp436.discord.bot.template.standard.service;

import com.z3rp436.discord.core.BotPlugin;
import com.z3rp436.discord.core.PluginRegistry;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class StandardBotPluginRegistry {

    private final PluginRegistry delegate;

    public StandardBotPluginRegistry(Collection<BotPlugin> plugins) {
        this.delegate = new PluginRegistry();
        plugins.forEach(delegate::register);
    }

    public PluginRegistry delegate() {
        return delegate;
    }
}

