package com.email.email_service.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailRequestDTO {

    @NotBlank(message = "O campo 'nome não pode ser vazio.")
    @Size(max = 40, message = "O campo 'nome' deve ter no máximo 40 caracteres")
    private String name;

    @NotBlank(message = "O campo 'email' não pode ser vazio.")
    @Email(message = "O formato do 'email' é inválido.")
    @Size(max = 60, message = "O campo 'email' deve ter no máximo 60 caracteres.")
    private String email;

    @NotBlank(message = "O campo 'subject' não pode ser vazio.")
    @Size(max = 40, message = "O campo 'subject' deve ter no máximo 40 caracteres.")
    private String subject;

    @NotBlank(message = "O campo 'message' não pode ser vazio.")
    @Size(max = 800, message = "O campo 'message' deve ter no máximo 800 caracteres.")
    private String message;
}
