package com.z3rp436.discord.bot.api.web.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface BotLifecycleApi {

    @PostMapping("/{botId}/lifecycle")
    Map<String, String> lifecycle(@PathVariable String botId, @Valid @RequestBody LifecycleRequest request);

    record LifecycleRequest(
            @NotBlank String action,
            @NotBlank String profile,
            @NotBlank String requestedBy
    ) {
    }
}

