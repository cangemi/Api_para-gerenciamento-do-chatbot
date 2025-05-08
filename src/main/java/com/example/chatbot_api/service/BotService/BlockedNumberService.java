package com.example.chatbot_api.service.BotService;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.chatbot_api.domain.Bot.BlockedNumber;
import com.example.chatbot_api.domain.Bot.Bot;
import com.example.chatbot_api.dto.Bot.BlockedNumberDTO;
import com.example.chatbot_api.repositories.Bot.BlockedNumberRepository;
import com.example.chatbot_api.repositories.Bot.BotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockedNumberService {

    private final BlockedNumberRepository blockedNumberRepository;
    private final BotRepository botRepository;

    public List<BlockedNumber> getAllBlockedNumbers(String botPublicId) {
        return blockedNumberRepository.findAllByBot_PublicId(botPublicId)
            .orElseThrow(() -> new RuntimeException("Nenhum número bloqueado encontrado"));
    }

    public List<BlockedNumber> createBlockedNumber(String botPublicId, BlockedNumberDTO dto) {
        Bot bot = botRepository.findBotByPublicId(botPublicId)
            .orElseThrow(() -> new RuntimeException("Bot não encontrado"));

        BlockedNumber blockedNumber = new BlockedNumber();
        blockedNumber.setNumber(dto.number());
        blockedNumber.setBot(bot);


        String clientId = bot.getNumber() + "-" + bot.getName();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String updateUrl = "http://localhost:3000/update-blocked/" + clientId;
            //String updateUrl = "http://whatsapp-node:3000/update-blocked/" + clientId;

            Map<String, Object> payload = Map.of("blockedNumber", dto.number());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            restTemplate.postForEntity(updateUrl, request, Void.class);
        } catch (Exception e) {
            
            System.err.println("Erro ao atualizar números bloqueados no servidor externo: " + e.getMessage());
        }

        blockedNumberRepository.save(blockedNumber);

        return blockedNumberRepository.findAllByBot_PublicId(botPublicId)
            .orElseThrow(() -> new RuntimeException("Nenhum número bloqueado encontrado"));
    }

    public List<BlockedNumber> deleteBlockedNumber(String publicId) {

        BlockedNumber blockedNumber = blockedNumberRepository.findByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("Número bloqueado não encontrado"));

        String clientId = blockedNumber.getBot().getNumber() + "-" + blockedNumber.getBot().getName();
        //String sessionUrl = "http://whatsapp-node:3000/delete/" + clientId;
        String sessionUrl = "http://localhost:3000/delete/" + clientId;
        String botPublicId = blockedNumber.getBot().getPublicId();

        Map<String, String> payload = Map.of("blockedNumber", blockedNumber.getNumber());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(
                sessionUrl,
                HttpMethod.DELETE,
                request,
                Void.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar número bloqueado no serviço externo", e);
        }

        blockedNumberRepository.delete(blockedNumber);

        return blockedNumberRepository.findAllByBot_PublicId(botPublicId)
            .orElseThrow(() -> new RuntimeException("Nenhum número bloqueado restante para este bot"));
    }
}