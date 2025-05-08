package com.example.chatbot_api.service.BotService;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.chatbot_api.domain.Bot.Bot;
import com.example.chatbot_api.domain.Organization.Organization;
import com.example.chatbot_api.dto.Bot.BotCreationResult;
import com.example.chatbot_api.dto.Bot.BotDTO;
import com.example.chatbot_api.repositories.Bot.BotRepository;
import com.example.chatbot_api.repositories.Organization.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BotService {

    private final BotRepository botRepository;
    private final OrganizationRepository organizationRepository;

    public List<Bot> getAllBots(String organizationId){
        return botRepository.findAllByOrganization_PublicId(organizationId)
            .orElseThrow(() -> new RuntimeException("Nenhum bot encontrado"));
    }


    public BotCreationResult createBot(String organizationId, BotDTO botDTO){
        
        Organization organization = organizationRepository.findOrganizationByPublicId(organizationId)
            .orElseThrow(() -> new RuntimeException("Nenhuma organização encontrada"));
            
        Bot bot = new Bot();
        bot.setName(botDTO.name());
        bot.setNumber(botDTO.number());
        bot.setRole(botDTO.role());
        bot.setOrganization(organization);


        String clientId = botDTO.number() + "-" + botDTO.name();
        
        RestTemplate restTemplate = new RestTemplate();
        String sessionUrl = "http://localhost:3000/session";
        //String sessionUrl = "http://whatsapp-node:3000/session";

        Map<String, String> payload = Map.of("clientId", clientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        String qrCode;
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(sessionUrl, request, Map.class);
            qrCode = (String) response.getBody().get("qrCode");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar QR Code", e);
        }

        botRepository.save(bot);

        List<Bot> bots = botRepository.findAllByOrganization_PublicId(organizationId)
        .orElseThrow(() -> new RuntimeException("Nenhum bot encontrado"));

        return new BotCreationResult(bots, qrCode);
    }


    public List<Bot> updateBot(String publicId, BotDTO botDTO){

        Bot bot = botRepository.findBotByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("Nenhum bot encontrado"));

        bot.setName(botDTO.name());
        bot.setNumber(botDTO.number());
        bot.setRole(botDTO.role());

        botRepository.save(bot);

        return botRepository.findAllByOrganization_PublicId(bot.getOrganization().getPublicId())
            .orElseThrow(() -> new RuntimeException("Nenhum bot encontrado"));
    }

    public List<Bot> deleteBot(String publicId){
        Bot bot = botRepository.findBotByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("Nenhum bot encontrado"));

        String clientId = bot.getNumber() + "-" + bot.getName();

        String sessionUrl = "http://localhost:3000/session/delete/" + clientId;

        //String sessionUrl = "http://whatsapp-node:3000/session/delete/" + clientId;
        

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.delete(sessionUrl); // Faz DELETE sem body
        } catch (Exception e) {
            throw new RuntimeException("Erro ao encerrar sessão do bot", e);
        }

        String oraganizationId = bot.getOrganization().getPublicId();

        botRepository.delete(bot);

        return botRepository.findAllByOrganization_PublicId(oraganizationId)
            .orElseThrow(() -> new RuntimeException("Nenhum bot encontrado"));
    }
}
