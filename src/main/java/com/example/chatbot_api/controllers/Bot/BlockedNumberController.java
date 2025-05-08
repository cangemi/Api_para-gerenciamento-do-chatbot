package com.example.chatbot_api.controllers.Bot;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.example.chatbot_api.domain.Bot.BlockedNumber;
import com.example.chatbot_api.dto.Bot.BlockedNumberDTO;
import com.example.chatbot_api.service.BotService.BlockedNumberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blocked-number")
public class BlockedNumberController {

    private final BlockedNumberService blockedNumberService;

    @GetMapping
    public ResponseEntity<List<BlockedNumberDTO>> getAllBlockedNumbers(@RequestParam String botId) {
        List<BlockedNumberDTO> list = blockedNumberService.getAllBlockedNumbers(botId).stream()
            .map(bn -> new BlockedNumberDTO(
                bn.getPublicId(),
                bn.getNumber()
                ))
            .toList();

        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<List<BlockedNumberDTO>> createBlockedNumber(
            @RequestParam String botId,
            @RequestBody BlockedNumberDTO dto) {

        List<BlockedNumber> blockedNumbers = blockedNumberService.createBlockedNumber(botId, dto);

        List<BlockedNumberDTO> list = blockedNumbers.stream()
            .map(bn -> new BlockedNumberDTO(
                bn.getPublicId(),
                bn.getNumber()
                ))
            .toList();
            
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<List<BlockedNumberDTO>> deleteBlockedNumber(@PathVariable String publicId) {

        
        List<BlockedNumberDTO> list = blockedNumberService.deleteBlockedNumber(publicId).stream()
            .map(bn -> new BlockedNumberDTO(
                bn.getPublicId(),
                bn.getNumber()
                ))
            .toList();
        return ResponseEntity.ok(list);
    }
}
