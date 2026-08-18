package com.z3rp436.discord.bot.main.web;

import com.z3rp436.discord.bot.main.config.BotMetadataProperties;
import com.z3rp436.discord.bot.main.service.MainBotPluginRegistry;
import com.z3rp436.discord.core.BotDescriptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/bot")
public class BotManifestController {

    private final BotMetadataProperties metadata;
    private final MainBotPluginRegistry registry;

    public BotManifestController(BotMetadataProperties metadata, MainBotPluginRegistry registry) {
        this.metadata = metadata;
        this.registry = registry;
    }

    @GetMapping("/manifest")
    public BotDescriptor manifest() {
        return new BotDescriptor(
                metadata.id(),
                metadata.displayName(),
                registry.delegate().all().stream().map(p -> p.pluginName()).collect(Collectors.toSet()),
                metadata.defaultProfile()
        );
    }
}

