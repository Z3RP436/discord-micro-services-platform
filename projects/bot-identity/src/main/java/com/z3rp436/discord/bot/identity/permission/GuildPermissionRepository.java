package com.z3rp436.discord.bot.identity.permission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuildPermissionRepository extends JpaRepository<GuildPermissionEntity, Long> {

    Optional<GuildPermissionEntity> findByGuildIdAndUserIdAndPermission(String guildId, String userId, String permission);
}

