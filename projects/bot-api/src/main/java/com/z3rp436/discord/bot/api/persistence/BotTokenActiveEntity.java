package com.z3rp436.discord.bot.api.persistence;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "bot_token_active", uniqueConstraints = @UniqueConstraint(name = "uk_bot_token_active_bot", columnNames = "botId"))
public class BotTokenActiveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String botId;

    @Column(nullable = false, length = 2048)
    private String tokenCipher;

    @Column(nullable = false, length = 256)
    private String tokenFingerprint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BotTokenStatus tokenStatus;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private Instant rotatedAt;

    private String rotatedBy;

    private Instant lastValidatedAt;

    private Instant lastStartAttemptAt;

    protected BotTokenActiveEntity() {
    }

    public BotTokenActiveEntity(String botId, String tokenCipher, String tokenFingerprint, String updatedBy) {
        Instant now = Instant.now();
        this.botId = botId;
        this.tokenCipher = tokenCipher;
        this.tokenFingerprint = tokenFingerprint;
        this.tokenStatus = BotTokenStatus.ACTIVE;
        this.createdAt = now;
        this.updatedAt = now;
        this.rotatedAt = now;
        this.rotatedBy = updatedBy;
    }

    public String botId() {
        return botId;
    }

    public String tokenCipher() {
        return tokenCipher;
    }

    public String tokenFingerprint() {
        return tokenFingerprint;
    }

    public BotTokenStatus tokenStatus() {
        return tokenStatus;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public void replaceToken(String tokenCipher, String tokenFingerprint, String updatedBy) {
        Instant now = Instant.now();
        this.tokenCipher = tokenCipher;
        this.tokenFingerprint = tokenFingerprint;
        this.tokenStatus = BotTokenStatus.ACTIVE;
        this.updatedAt = now;
        this.rotatedAt = now;
        this.rotatedBy = updatedBy;
    }

    public void markStartAttempt() {
        this.lastStartAttemptAt = Instant.now();
    }

    public void setLastValidatedAt(Instant lastValidatedAt) {
        this.lastValidatedAt = lastValidatedAt;
    }
}

