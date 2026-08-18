package com.z3rp436.discord.bot.identity.permission;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GuildPermissionControllerTest {

    @Test
    void shouldExposeRequestRecordFields() {
        GuildPermissionController.SetPermissionRequest request =
                new GuildPermissionController.SetPermissionRequest("moderation action", true);

        assertThat(request.reason()).isEqualTo("moderation action");
        assertThat(request.allowed()).isTrue();
    }
}


