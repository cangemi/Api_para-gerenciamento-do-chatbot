package com.example.chatbot_api.dto.Bot;

import java.util.List;

import com.example.chatbot_api.domain.Bot.Bot;

public record BotCreationResult(List<Bot> bots, String qrCode) {}
