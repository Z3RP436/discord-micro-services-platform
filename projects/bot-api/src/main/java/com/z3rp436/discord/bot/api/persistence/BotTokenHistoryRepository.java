package com.z3rp436.discord.bot.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BotTokenHistoryRepository extends JpaRepository<BotTokenHistoryEntity, Long> {
}

