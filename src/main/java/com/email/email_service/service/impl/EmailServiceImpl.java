package com.email.email_service.service.impl;

import com.email.email_service.DTO.EmailRequestDTO;
import com.email.email_service.service.EmailService;
import jakarta.mail.internet.MimeMessage;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${app.email.to}")
    private String emailTo;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendSimpleEmail(EmailRequestDTO emailRequest) {

        log.info("Iniciando processo de envio de e-mail para: {}", emailTo);

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String sanitizedName = Jsoup.clean(emailRequest.getName(), Safelist.none());
            String sanitizedSubject = Jsoup.clean(emailRequest.getSubject(), Safelist.none());
            String sanitizedMessage = Jsoup.clean(emailRequest.getMessage(), Safelist.none());

            helper.setTo(emailTo);

            helper.setSubject(sanitizedSubject);

            String fromEmail = emailRequest.getEmail();
            helper.setFrom(sanitizedName + " <" + fromEmail + ">");

            String htmlBody = " <html><body>"
                    + "<h1>Nova Mensagem de Contato</h1>"
                    + "<p><b>Nome:</b> " + sanitizedName + "</p>"
                    + "<p><b>E-mail de Origem:</b> " + fromEmail + "</p>"
                    + "<p><b>Assunto:</b> " + sanitizedSubject + "</p>"
                    + "<hr>"
                    + "<p><b>Mensagem:</b></p>"
                    + "<p style=\"white-space: pre-wrap;\">" + sanitizedMessage + "</p>" // 'pre-wrap' preserva quebras de linha
                    + "</body></html>";

            helper.setText(htmlBody, true);

            mailSender.send(message);
            log.info("E-mail enviado com sucesso de {} para {}", fromEmail, emailTo);
        } catch (Exception e) {

            log.error("Falha ao enviar e-mail de {}: {}", emailRequest.getEmail(), e.getMessage());

        }
    }
}
