package com.example.chatbot_api.repositories.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatbot_api.domain.User.User;

public interface UserRepository extends JpaRepository< User, String> {

    Optional<User> findByEmail(String email);

}
