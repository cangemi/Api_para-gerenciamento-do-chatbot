package com.example.chatbot_api.repositories.Bot;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatbot_api.domain.Bot.Bot;

public interface BotRepository extends JpaRepository<Bot, Long> {


    Optional<Bot> findBotByPublicId(String publicId);
    
    Optional<Bot> findBotByNumber(String number);

    Optional <List<Bot>> findAllByOrganization_PublicId(String oraganizationId);

}
