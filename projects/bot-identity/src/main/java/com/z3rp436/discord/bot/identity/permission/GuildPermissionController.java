package com.z3rp436.discord.bot.identity.permission;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/identity/guilds")
public class GuildPermissionController {

    private final GuildPermissionRepository repository;

    public GuildPermissionController(GuildPermissionRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/{guildId}/users/{userId}/permissions/{permission}")
    public Map<String, Object> setPermission(
            @PathVariable String guildId,
            @PathVariable String userId,
            @PathVariable String permission,
            @Valid @RequestBody SetPermissionRequest request
    ) {
        GuildPermissionEntity entity = repository.findByGuildIdAndUserIdAndPermission(guildId, userId, permission)
                .orElse(new GuildPermissionEntity(guildId, userId, permission, request.allowed()));
        entity.setAllowed(request.allowed());
        repository.save(entity);

        return Map.of(
                "guildId", guildId,
                "userId", userId,
                "permission", permission,
                "allowed", request.allowed()
        );
    }

    @GetMapping("/{guildId}/users/{userId}/permissions/{permission}")
    public Map<String, Object> getPermission(
            @PathVariable String guildId,
            @PathVariable String userId,
            @PathVariable String permission
    ) {
        boolean allowed = repository.findByGuildIdAndUserIdAndPermission(guildId, userId, permission)
                .map(GuildPermissionEntity::allowed)
                .orElse(false);

        return Map.of(
                "guildId", guildId,
                "userId", userId,
                "permission", permission,
                "allowed", allowed
        );
    }

    public record SetPermissionRequest(@NotBlank String reason, boolean allowed) {
    }
}

