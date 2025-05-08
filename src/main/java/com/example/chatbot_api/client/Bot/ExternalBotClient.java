package com.example.chatbot_api.client.Bot;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ExternalBotClient {

    private final RestTemplate restTemplate;

    public ExternalBotClient() {
        this.restTemplate = new RestTemplate();
    }

    public void updateRag(String publicId) {
        String updateUrl = "http://localhost:5000/update-rag"; // idealmente vir de application.properties/yml

        try {
            Map<String, String> payload = Map.of("public_id", publicId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

            restTemplate.put(updateUrl, request, Void.class);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar a info no servidor externo: " + e.getMessage());
        }
    }
}
