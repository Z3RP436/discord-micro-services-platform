package com.z3rp436.discord.core;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PluginRegistry {

    private final Map<String, BotPlugin> plugins = new ConcurrentHashMap<>();

    public void register(BotPlugin plugin) {
        plugins.put(plugin.pluginName(), plugin);
    }

    public Optional<BotPlugin> findByName(String pluginName) {
        return Optional.ofNullable(plugins.get(pluginName));
    }

    public Collection<BotPlugin> all() {
        return plugins.values();
    }
}

