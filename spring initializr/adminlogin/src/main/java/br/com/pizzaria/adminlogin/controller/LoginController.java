package br.com.pizzaria.adminlogin.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.pizzaria.adminlogin.config.DatabaseConnection;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.getOrDefault("username", "").trim();
        String password = credentials.getOrDefault("password", "").trim();

        Map<String, String> response = new HashMap<>();

        if (username.isEmpty() || password.isEmpty()) {
            response.put("status", "erro");
            response.put("mensagem", "Campos obrigatórios não preenchidos");
            return ResponseEntity.badRequest().body(response);
        }

        boolean authenticated = DatabaseConnection.checkCredentials(username, password);

        if (authenticated) {
            session.setAttribute("adminLogado", true);
            response.put("status", "ok");
            response.put("mensagem", "Login realizado com sucesso");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "erro");
            response.put("mensagem", "Usuário ou senha inválidos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate(); 
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("mensagem", "Logout efetuado");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/status")
    public ResponseEntity<Map<String, String>> verificarLogin(HttpSession session) {
        Boolean admin = (Boolean) session.getAttribute("adminLogado");
        Map<String, String> response = new HashMap<>();

        if (admin != null && admin) {
            response.put("status", "ok");
            response.put("mensagem", "Admin está logado");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "erro");
            response.put("mensagem", "Acesso não autorizado");
            return ResponseEntity.status(403).body(response);
        }
    }
}
