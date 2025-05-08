package com.example.chatbot_api.service.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.chatbot_api.domain.User.PasswordResetToken;
import com.example.chatbot_api.domain.User.User;
import com.example.chatbot_api.repositories.User.PasswordResetTokenRepository;
import com.example.chatbot_api.repositories.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepo;

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;

    public void createPasswordResetToken(String email) {
        Optional<User> optionalUser = this.userRepo.findByEmail(email);
        if (!optionalUser.isEmpty()) {
            User user = optionalUser.get();
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // expira em 1 hora

            tokenRepo.save(resetToken);

            // Enviar e-mail
            sendResetEmail(user.getEmail(), token);
        }
    }
    private void sendResetEmail(String email, String token) {
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
    
        System.out.println("==== Simulando envio de e-mail ====");
        System.out.println("Para: " + email);
        System.out.println("Assunto: Redefinição de senha");
        System.out.println("Corpo: Clique no link para redefinir sua senha: " + resetLink);
        System.out.println("====================================");
    }


    public String resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = tokenRepo.findByToken(token);

        if (optionalToken.isEmpty()) {
            return "Token inválido";
        }

        PasswordResetToken resetToken = optionalToken.get();

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Token expirado";
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        tokenRepo.delete(resetToken);

        return "Senha redefinida com sucesso";
    }
}
