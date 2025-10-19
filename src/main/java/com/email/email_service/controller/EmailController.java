package com.email.email_service.controller;

import com.email.email_service.DTO.EmailRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/simple")
    public ResponseEntity<String> sendSimpleEmail(@Valid @RequestBody EmailRequestDTO emailRequest) {

        // ---- ATENÇÃO ----
        // Esta linha abaixo VAI DAR UM ERRO DE COMPILAÇÃO agora.
        // Isso é esperado!
        // Nosso próximo passo será exatamente corrigir isso, atualizando a
        // interface 'EmailService' para que ela também aceite 'EmailRequestDTO'.
        emailService.sendSimpleEmail(emailRequest);

        // Retorna uma resposta HTTP 200 OK com uma mensagem clara.
        return ResponseEntity.ok("Solicitação de e-mail recebida. O envio está sendo processado.");
    }
}
