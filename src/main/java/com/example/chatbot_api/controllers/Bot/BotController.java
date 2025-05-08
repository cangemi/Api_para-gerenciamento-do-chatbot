package com.example.chatbot_api.controllers.Bot;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.chatbot_api.dto.Bot.BotCreationResult;
import com.example.chatbot_api.dto.Bot.BotDTO;
import com.example.chatbot_api.dto.Bot.BotResponseDTO;
import com.example.chatbot_api.service.BotService.BotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bot")
public class BotController {

    private final BotService botService;

    @GetMapping
    public ResponseEntity<List<BotDTO>> getAllBots(@RequestParam String organizationId) {
        List<BotDTO> bots = botService.getAllBots(organizationId).stream()
                .map(bot -> new BotDTO(
                    bot.getName(),
                    bot.getNumber(),
                    bot.getRole(),
                    bot.getPublicId()
                    ))
                .toList();

        return ResponseEntity.ok(bots);
    }

@PostMapping
public ResponseEntity<BotResponseDTO> createBot(
        @RequestParam String organizationId,
        @RequestBody BotDTO botDTO) {

    BotCreationResult result = botService.createBot(organizationId, botDTO);

    List<BotDTO> bots = result.bots().stream()
        .map(bot -> new BotDTO(
            bot.getName(),
            bot.getNumber(),
            bot.getRole(),
            bot.getPublicId()
        ))
        .toList();

    BotResponseDTO responseDTO = new BotResponseDTO(bots, result.qrCode());
    return ResponseEntity.ok(responseDTO);
}


    @PutMapping("/{publicId}")
    public ResponseEntity<List<BotDTO>> updateBot(
            @PathVariable String publicId,
            @RequestBody BotDTO botDTO) {

        List<BotDTO> bots = botService.updateBot(publicId, botDTO).stream()
            .map(bot -> new BotDTO(
                bot.getName(),
                bot.getNumber(),
                bot.getRole(),
                bot.getPublicId()
                ))
            .toList();

        return ResponseEntity.ok(bots);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<List<BotDTO>> deleteBot(@PathVariable String publicId) {
        List<BotDTO> bots = botService.deleteBot(publicId).stream()
            .map(bot -> new BotDTO(
                bot.getName(),
                bot.getNumber(),
                bot.getRole(),
                bot.getPublicId()
                ))
            .toList();

        return ResponseEntity.ok(bots);
    }
}