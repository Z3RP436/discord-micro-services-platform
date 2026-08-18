package com.z3rp436.discord.bot.api.persistence;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "bot_token_history")
public class BotTokenHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String botId;

    @Column(nullable = false, length = 256)
    private String tokenFingerprint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BotTokenHistoryStatus tokenStatus;

    @Column(nullable = false)
    private Instant archivedAt;

    private String archivedBy;

    private String reason;

    protected BotTokenHistoryEntity() {
    }

    public BotTokenHistoryEntity(
            String botId,
            String tokenFingerprint,
            BotTokenHistoryStatus tokenStatus,
            String archivedBy,
            String reason
    ) {
        this.botId = botId;
        this.tokenFingerprint = tokenFingerprint;
        this.tokenStatus = tokenStatus;
        this.archivedAt = Instant.now();
        this.archivedBy = archivedBy;
        this.reason = reason;
    }
}

