package com.z3rp436.discord.bot.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BotTokenActiveRepository extends JpaRepository<BotTokenActiveEntity, Long> {

    Optional<BotTokenActiveEntity> findByBotId(String botId);

    void deleteByBotId(String botId);
}

