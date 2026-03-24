package br.com.pizzaria.adminlogin.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
public class PasswordRecoveryController {


    @PostMapping("/recovery-password")
    public ResponseEntity<String> recoverPassword(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "").trim();

        if (username.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Username obrigatório");
        }
        
        String responseMessage = "Se o usuário existir, enviaremos instruções por e-mail.";
        return ResponseEntity.ok(responseMessage);
    }
}
