package com.example.chatbot_api.controllers.User;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatbot_api.domain.User.User;
import com.example.chatbot_api.repositories.User.UserRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserRepository userRepository;


    @GetMapping
    public ResponseEntity <String> getUser(){
        return ResponseEntity.ok("Sucesso!");
    }

}
