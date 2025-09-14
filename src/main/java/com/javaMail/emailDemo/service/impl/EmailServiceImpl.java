package com.javaMail.emailDemo.service.impl;

import com.javaMail.emailDemo.entity.Mail;
import com.javaMail.emailDemo.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendSimpleEmail(Mail mail) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo("hitaludcosta@mail.com");

            helper.setSubject(mail.getSubject());

            helper.setFrom("hitaludcosta@gmail.com", mail.getName() + " <" + mail.getEmail());

            String htmlBody = "<html<body>"
                            + "<h1>Nova Mensagem de Contato</hh1>"
                            + "<p><b>Nome:</b> " + mail.getName() + "</p>"
                            + "<p><b>E-mail:</b> " + mail.getEmail() + "</p>"
                            + "<p><b>Assunto:</b> " + mail.getSubject() + "</p>"
                            + "--------------"
                            + "<p><b>Mensagem:</b></p>"
                            + "<p>" + mail.getMessage() + "<p>"
                            + "</body></html>";

            helper.setText(htmlBody, true);

            mailSender.send(message);

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
