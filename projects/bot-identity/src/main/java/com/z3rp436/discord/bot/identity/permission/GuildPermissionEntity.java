package com.z3rp436.discord.bot.identity.permission;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "guild_permission")
public class GuildPermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String guildId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String permission;

    @Column(nullable = false)
    private boolean allowed;

    protected GuildPermissionEntity() {
    }

    public GuildPermissionEntity(String guildId, String userId, String permission, boolean allowed) {
        this.guildId = guildId;
        this.userId = userId;
        this.permission = permission;
        this.allowed = allowed;
    }

    public String guildId() {
        return guildId;
    }

    public String userId() {
        return userId;
    }

    public String permission() {
        return permission;
    }

    public boolean allowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}
