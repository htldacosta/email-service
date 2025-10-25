package com.email.email_service.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.email.email_service.DTO.EmailRequestDTO;
import com.email.email_service.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {


    @Mock
    private JavaMailSender mailSender;


    @Mock
    private MimeMessage mimeMessage;


    @InjectMocks
    private EmailServiceImpl emailService;


    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(emailService, "emailTo", "teste.destino@exemplo.com");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendSimpleEmail_Success() {

        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setName("Usuário Teste");
        requestDTO.setEmail("remetente@teste.com");
        requestDTO.setSubject("Assunto de Teste");
        requestDTO.setMessage("Esta é uma mensagem de teste.");


        emailService.sendSimpleEmail(requestDTO);


        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendSimpleEmail_SanitizationShouldWork() {

        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setName("<h1>Nome</h1>");
        requestDTO.setEmail("remetente@teste.com");
        requestDTO.setSubject("<script>alert('XSS')</script>Assunto");
        requestDTO.setMessage("<p>Mensagem com tag</p>");


        emailService.sendSimpleEmail(requestDTO);


        verify(mailSender, times(1)).send(mimeMessage);

    }

    @Test
    void testSendSimpleEmail_MailSenderThrowsException() {

        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setName("Usuário Teste");
        requestDTO.setEmail("remetente@teste.com");
        requestDTO.setSubject("Assunto");
        requestDTO.setMessage("Mensagem");


        doThrow(new RuntimeException("Erro simulado de SMTP"))
                .when(mailSender).send(mimeMessage);


        assertDoesNotThrow(() -> {
            emailService.sendSimpleEmail(requestDTO);
        });

        verify(mailSender, times(1)).send(mimeMessage);
    }
}
