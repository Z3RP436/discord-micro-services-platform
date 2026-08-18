package com.z3rp436.discord.bot.api.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bot_profile_override")
public class BotProfileOverrideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String botId;

    @Column(nullable = false)
    private String guildId;

    @Column(nullable = false)
    private String profile;

    protected BotProfileOverrideEntity() {
    }

    public BotProfileOverrideEntity(String botId, String guildId, String profile) {
        this.botId = botId;
        this.guildId = guildId;
        this.profile = profile;
    }

    public String botId() {
        return botId;
    }

    public String guildId() {
        return guildId;
    }

    public String profile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
