package com.z3rp436.discord.bot.orchestrator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class BotStartGuardClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String botApiBaseUrl;

    public BotStartGuardClient(@Value("${bot-api.base-url:http://bot-api:8081}") String botApiBaseUrl) {
        this.botApiBaseUrl = botApiBaseUrl;
    }

    public GuardResult evaluate(String botId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(botApiBaseUrl + "/api/bots/" + botId + "/token"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 409) {
                return new GuardResult(false, "BOT_NOT_RUNNABLE");
            }
            if (response.statusCode() >= 400) {
                return new GuardResult(false, "TOKEN_MISSING");
            }

            JsonNode node = objectMapper.readTree(response.body());
            boolean configured = node.path("tokenConfigured").asBoolean(false);
            String tokenStatus = node.path("tokenStatus").asText("");

            if (!configured) {
                return new GuardResult(false, "TOKEN_MISSING");
            }
            if (!"ACTIVE".equals(tokenStatus)) {
                return new GuardResult(false, "TOKEN_INVALID");
            }
            return new GuardResult(true, null);
        } catch (Exception ex) {
            return new GuardResult(false, "TOKEN_MISSING");
        }
    }

    public record GuardResult(boolean allowed, String reasonCode) {
    }
}


