package com.email.email_service.service;

import com.email.email_service.DTO.EmailRequestDTO;

public interface EmailService {

    void sendSimpleEmail(EmailRequestDTO emailRequest);
}
