package com.z3rp436.discord.bot.api.web.api;

import com.z3rp436.discord.bot.api.service.BotDirectoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface BotManagementApi extends BotLifecycleApi {

    @GetMapping
    List<BotDirectoryService.BotView> allBots();

    @PutMapping("/{botId}/guilds/{guildId}/profile")
    Map<String, String> setProfileOverride(
            @PathVariable String botId,
            @PathVariable String guildId,
            @Valid @RequestBody SetProfileRequest request
    );

    record SetProfileRequest(@NotBlank String profile) {
    }
}

