package br.com.pizzaria.adminlogin.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.pizzaria.adminlogin.config.DatabaseConnection;

import java.util.Map;

@RestController
public class PasswordRecoveryController {

    @PostMapping("/recupera-senha")
    public ResponseEntity<String> recoverPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String email = DatabaseConnection.findEmailByUsername(username);

        if (email != null) {
            return ResponseEntity.ok("Instruções enviadas para o e-mail cadastrado.");
        } else {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }
    }
}
