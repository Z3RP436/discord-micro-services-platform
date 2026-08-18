package com.z3rp436.discord.bot.identity.permission;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GuildPermissionEntityTest {

    @Test
    void shouldUpdatePermissionFlag() {
        GuildPermissionEntity entity = new GuildPermissionEntity("guild-1", "user-1", "BAN", false);

        entity.setAllowed(true);

        assertThat(entity.guildId()).isEqualTo("guild-1");
        assertThat(entity.userId()).isEqualTo("user-1");
        assertThat(entity.permission()).isEqualTo("BAN");
        assertThat(entity.allowed()).isTrue();
    }
}

