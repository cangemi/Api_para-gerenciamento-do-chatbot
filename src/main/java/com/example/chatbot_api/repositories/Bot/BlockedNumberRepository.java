package com.example.chatbot_api.repositories.Bot;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatbot_api.domain.Bot.BlockedNumber;


public interface BlockedNumberRepository extends JpaRepository<BlockedNumber, Long> {

    Optional<BlockedNumber> findByPublicId(String publicId);

    Optional <List<BlockedNumber>> findAllByBot_PublicId(String blockedNumberId);

}
