package com.email.email_service.controller;

import com.email.email_service.DTO.EmailRequestDTO;
import com.email.email_service.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// --- CORREÇÃO 1: Garantir que a importação do @Value está correta ---
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
// --- CORREÇÃO 2: Adicionar a importação do MediaType ---
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

    // Converte objetos Java (DTO) para JSON
    @Autowired
    private ObjectMapper objectMapper;

    // Simula o EmailService. Não queremos enviar e-mails de
    // verdade durante o teste do controller.
    @MockBean
    private EmailService emailService;

    // Pega a API Key do arquivo "src/test/resources/application.properties"
    @Value("${app.api-key}")
    private String apiKey;

    private final String API_KEY_HEADER = "minha-chave"; // O nome do header que o interceptor espera

    @Test
    void testSendSimpleEmail_Success_ShouldReturnOk() throws Exception {
        // 1. Arrange (Preparar dados válidos)
        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setName("Nome Valido");
        requestDTO.setEmail("teste@dominio.com");
        requestDTO.setSubject("Assunto Valido");
        requestDTO.setMessage("Mensagem com mais de 10 caracteres."); // Baseado no DTO

        // 2. Act (Executar) & 3. Assert (Verificar)
        mockMvc.perform(MockMvcRequestBuilders.post("/simple") // Faz um POST para /simple
                        .header(API_KEY_HEADER, apiKey) // Adiciona a API Key correta
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))) // Converte o DTO para JSON
                .andExpect(status().isOk()); // Espera a resposta HTTP 200 OK
    }

    @Test
    void testSendSimpleEmail_InvalidBody_ShouldReturnBadRequest() throws Exception {
        // 1. Arrange (Preparar dados inválidos - nome em branco)
        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setName(""); // <-- INVÁLIDO (NotBlank)
        requestDTO.setEmail("teste@dominio.com");
        requestDTO.setSubject("Assunto Valido");
        requestDTO.setMessage("Mensagem com mais de 10 caracteres.");

        // 2. Act & 3. Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/simple")
                        .header(API_KEY_HEADER, apiKey) // Chave correta
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest()) // Espera HTTP 400 Bad Request
                // Verifica se o JSON de erro do GlobalExceptionHandler está correto
                .andExpect(jsonPath("$.name").value("O campo 'Nome não pode ser vazio."));
    }

    @Test
    void testSendSimpleEmail_NoApiKey_ShouldReturnUnauthorized() throws Exception {
        // 1. Arrange (DTO Válido)
        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setName("Nome Valido");
        requestDTO.setEmail("teste@dominio.com");
        requestDTO.setSubject("Assunto Valido");
        requestDTO.setMessage("Mensagem com mais de 10 caracteres.");

        // 2. Act & 3. Assert
        // Executa a requisição SEM o cabeçalho da API Key
        mockMvc.perform(MockMvcRequestBuilders.post("/simple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnauthorized()); // Espera HTTP 401 Unauthorized
    }


}
