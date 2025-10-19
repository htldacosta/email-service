package com.email.email_service.controller;

import com.email.email_service.DTO.EmailRequestDTO;
import com.email.email_service.service.EmailService;
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

        emailService.sendSimpleEmail(emailRequest);


        return ResponseEntity.ok("Solicitação de e-mail recebida. O envio está sendo processado.");
    }
}
