package com.email.email_service.controller;

import com.email.email_service.DTO.EmailRequestDTO;
import com.email.email_service.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private EmailService emailService;


    @Value("${app.api-key}")
    private String apiKey;

    private final String API_KEY_HEADER = "minha-chave";

    @Test
    void testSendSimpleEmail_Success_ShouldReturnOk() throws Exception {

        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setName("Nome Valido");
        requestDTO.setEmail("teste@dominio.com");
        requestDTO.setSubject("Assunto Valido");
        requestDTO.setMessage("Mensagem com mais de 10 caracteres."); // Baseado no DTO


        mockMvc.perform(MockMvcRequestBuilders.post("/simple")
                        .header(API_KEY_HEADER, apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testSendSimpleEmail_InvalidBody_ShouldReturnBadRequest() throws Exception {

        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setName("");
        requestDTO.setEmail("teste@dominio.com");
        requestDTO.setSubject("Assunto Valido");
        requestDTO.setMessage("Mensagem com mais de 10 caracteres.");


        mockMvc.perform(MockMvcRequestBuilders.post("/simple")
                        .header(API_KEY_HEADER, apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("O campo 'Nome não pode ser vazio."));
    }

    @Test
    void testSendSimpleEmail_NoApiKey_ShouldReturnUnauthorized() throws Exception {

        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setName("Nome Valido");
        requestDTO.setEmail("teste@dominio.com");
        requestDTO.setSubject("Assunto Valido");
        requestDTO.setMessage("Mensagem com mais de 10 caracteres.");


        mockMvc.perform(MockMvcRequestBuilders.post("/simple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnauthorized());
    }


}
