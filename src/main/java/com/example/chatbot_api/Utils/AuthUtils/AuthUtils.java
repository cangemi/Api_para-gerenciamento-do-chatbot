package com.example.chatbot_api.Utils.AuthUtils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.chatbot_api.domain.User.User;

public class AuthUtils {
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
    }

    public static String getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
