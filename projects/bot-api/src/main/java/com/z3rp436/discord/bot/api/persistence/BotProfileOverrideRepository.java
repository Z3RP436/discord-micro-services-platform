package com.z3rp436.discord.bot.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BotProfileOverrideRepository extends JpaRepository<BotProfileOverrideEntity, Long> {

    Optional<BotProfileOverrideEntity> findByBotIdAndGuildId(String botId, String guildId);
}

