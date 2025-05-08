package com.example.chatbot_api.dto.Bot;

import java.util.List;

public record BotResponseDTO(List<BotDTO> bots, String qrCode) {}