package com.example.chatbot_api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.chatbot_api.domain.User.User;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                                .withIssuer("api-chatbot")
                                .withSubject(user.getEmail())
                                .withExpiresAt(this.generateExpirationDate())
                                .sign(algorithm);
        return token;
        } catch (JWTCreationException exception) {
            // TODO: handle exception
            throw new RuntimeException("Erro equanto estava autenticando");
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                            .withIssuer("api-chatbot")
                            .build()
                            .verify(token)
                            .getSubject();

        } catch (JWTVerificationException exception) {
            // TODO: handle exception
            return null;
        }
    }


    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-3"));
    }
}
