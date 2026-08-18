package com.z3rp436.discord.bot.main.web;

import com.z3rp436.discord.bot.main.service.MainBotPluginRegistry;
import com.z3rp436.discord.core.BotCommandContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/bot/plugins")
public class BotCommandController {

    private final MainBotPluginRegistry registry;

    public BotCommandController(MainBotPluginRegistry registry) {
        this.registry = registry;
    }

    @PostMapping("/{pluginName}/commands")
    public Map<String, String> execute(
            @PathVariable String pluginName,
            @Valid @RequestBody ExecutePluginCommandRequest request
    ) {
        return registry.delegate()
                .findByName(pluginName)
                .map(plugin -> Map.of("result", plugin.handle(new BotCommandContext(
                        request.guildId(),
                        request.userId(),
                        request.command(),
                        request.arguments()
                ))))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plugin not found: " + pluginName));
    }

    public record ExecutePluginCommandRequest(
            @NotBlank String guildId,
            @NotBlank String userId,
            @NotBlank String command,
            Map<String, String> arguments
    ) {
    }
}

