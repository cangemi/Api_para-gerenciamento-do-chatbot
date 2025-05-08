package com.example.chatbot_api.dto.User;

public record ResetPasswordDTO(String token, String newPassword) {
}