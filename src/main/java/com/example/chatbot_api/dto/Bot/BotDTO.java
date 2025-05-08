package com.example.chatbot_api.dto.Bot;

import com.example.chatbot_api.domain.Bot.Bot.BotRole;

public record BotDTO(String name, String number, BotRole role, String publicId) {}
