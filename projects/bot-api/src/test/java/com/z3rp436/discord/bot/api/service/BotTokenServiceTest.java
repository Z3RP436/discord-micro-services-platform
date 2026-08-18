package com.z3rp436.discord.bot.api.service;

import com.z3rp436.discord.bot.api.persistence.BotTokenActiveEntity;
import com.z3rp436.discord.bot.api.persistence.BotTokenActiveRepository;
import com.z3rp436.discord.bot.api.persistence.BotTokenHistoryRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BotTokenServiceTest {

    @Test
    void shouldReturnTokenMissingWhenNoTokenExists() {
        BotTokenActiveRepository activeRepository = mock(BotTokenActiveRepository.class);
        BotTokenHistoryRepository historyRepository = mock(BotTokenHistoryRepository.class);
        when(activeRepository.findByBotId("bot-main")).thenReturn(Optional.empty());

        BotTokenService service = new BotTokenService(activeRepository, historyRepository);

        BotTokenService.GuardResult result = service.evaluateStartGuard("bot-main");

        assertThat(result.allowed()).isFalse();
        assertThat(result.reasonCode()).isEqualTo("TOKEN_MISSING");
    }

    @Test
    void shouldStoreTokenAsProvidedString() {
        BotTokenActiveRepository activeRepository = mock(BotTokenActiveRepository.class);
        BotTokenHistoryRepository historyRepository = mock(BotTokenHistoryRepository.class);
        AtomicReference<BotTokenActiveEntity> savedEntity = new AtomicReference<>();

        when(activeRepository.findByBotId("bot-main")).thenReturn(Optional.empty());
        when(activeRepository.save(any(BotTokenActiveEntity.class))).thenAnswer(invocation -> {
            BotTokenActiveEntity entity = invocation.getArgument(0);
            savedEntity.set(entity);
            return entity;
        });

        BotTokenService service = new BotTokenService(activeRepository, historyRepository);
        service.upsert("bot-main", "plain-token-value", "test-user");

        assertThat(savedEntity.get()).isNotNull();
        assertThat(savedEntity.get().tokenCipher()).isEqualTo("plain-token-value");
    }
}


