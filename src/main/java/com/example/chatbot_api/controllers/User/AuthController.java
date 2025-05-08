package com.example.chatbot_api.controllers.User;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatbot_api.domain.User.PasswordResetToken;
import com.example.chatbot_api.domain.User.User;
import com.example.chatbot_api.dto.User.LoginRequestDTO;
import com.example.chatbot_api.dto.User.RegisterRequestDTO;
import com.example.chatbot_api.dto.User.ResetPasswordDTO;
import com.example.chatbot_api.dto.User.ResponseDTO;
import com.example.chatbot_api.infra.security.TokenService;
import com.example.chatbot_api.repositories.User.UserRepository;
import com.example.chatbot_api.service.User.PasswordResetService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordResetService passwordResetService;

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(()-> new RuntimeException("User not Found!"));
        
        if(passwordEncoder.matches(body.password(),user.getPassword())){
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }
    
    
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        
        Optional<User> user = this.repository.findByEmail(body.email());
        
        if(user.isEmpty()){
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));

        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        System.out.println("Entrou");
        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok("Se o e-mail estiver cadastrado, você receberá um link para redefinir sua senha.");
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto) {
        String result = passwordResetService.resetPassword(dto.token(), dto.newPassword());
    
        if (result.equals("Senha redefinida com sucesso")) {
            return ResponseEntity.ok(result);
        }
    
        return ResponseEntity.badRequest().body(result);
    }

}
