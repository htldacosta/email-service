# Email Service API

## Visão Geral

Este projeto é uma API RESTful simples construída com Spring Boot, projetada para enviar e-mails de forma assíncrona. Foi desenvolvido principalmente para servir como backend para o formulário de contato do meu site pessoal ([https://www.hitalu.com](https://www.hitalu.com)), permitindo que os visitantes enviem mensagens que serão encaminhadas para um e-mail pré-configurado.

A aplicação está hospedada na **AWS**, utilizando o serviço **Elastic Beanstalk** para execução e, idealmente, o **AWS SES (Simple Email Service)** para o envio de e-mails em produção (embora possa ser configurada com outros provedores SMTP como o GMail para desenvolvimento).

## Funcionalidades Implementadas

* **Endpoint RESTful:** Expõe um único endpoint `POST /simple` para receber solicitações de envio de e-mail.
* **Validação de Dados:** Utiliza `jakarta.validation` (`@NotBlank`, `@Email`, `@Size`) para garantir que os dados de entrada (nome, e-mail, assunto, mensagem) estejam presentes e dentro dos limites definidos no `EmailRequestDTO`.
* **Sanitização de Input:** Emprega a biblioteca `Jsoup` para limpar qualquer HTML potencialmente malicioso dos campos de texto (nome, assunto, mensagem) antes de montar o e-mail.
* **Envio Assíncrono:** O envio de e-mail é delegado a um método `@Async`, permitindo que a API responda rapidamente à solicitação enquanto o e-mail é processado em segundo plano.
* **Segurança:** O endpoint `/simple` é protegido por uma chave de API (`API Key`) que deve ser enviada no cabeçalho `minha-chave` da requisição.
* **CORS:** Configurado para permitir requisições `POST` vindas especificamente do domínio `https://www.hitalu.com`.
* **Tratamento de Exceções:** Possui um `GlobalExceptionHandler` que captura erros de validação e retorna respostas JSON claras com status `HTTP 400 Bad Request`.
* **Logging:** Utiliza SLF4J para registrar informações sobre o processo de envio e possíveis erros.
* **Testes:** Cobertura de testes unitários e de integração para as camadas de Controller e Service, utilizando JUnit 5, Mockito e Spring Test.

## Arquitetura

O projeto segue uma **Arquitetura em Camadas** padrão para aplicações Spring Boot:

* **Controller:** Recebe as requisições HTTP, valida o DTO e delega para o Service.
* **DTO (Data Transfer Object):** Define a estrutura dos dados recebidos na API e contém as anotações de validação.
* **Service (Interface e Implementação):** Contém a lógica de negócios principal (sanitização, montagem e envio do e-mail).
* **Config:** Classes de configuração para CORS, Interceptor de API Key e processamento Assíncrono.
* **Exception:** Handler global para exceções de validação.

É uma arquitetura **Monolítica**, pois todos os componentes rodam como um único serviço.

## Tecnologias Utilizadas

* **Java:** Versão 21
* **Spring Boot:** Versão 3.5.6
* **Maven:** Gerenciador de dependências e build.
* **Spring Web:** Para criação da API RESTful.
* **Spring Mail:** Para abstração do envio de e-mails.
* **Spring Validation:** Para validação dos dados de entrada.
* **Lombok:** Para reduzir código boilerplate (getters, setters, etc.).
* **Jsoup:** Para sanitização de HTML.
* **JUnit 5 / Mockito / Spring Test:** Para testes unitários e de integração.

## Configuração

Para rodar a aplicação localmente ou em produção, é necessário configurar as seguintes propriedades no arquivo `src/main/resources/application.properties` (ou através de variáveis de ambiente):

```properties
# Nome da aplicação (opcional)
spring.application.name=email-service

# Configuração do Servidor SMTP (Ex: AWS SES, GMail com Senha de App)
spring.mail.host=<seu-host-smtp>
spring.mail.port=<porta-smtp> # Ex: 587
spring.mail.username=<seu-usuario-smtp>
spring.mail.password=<sua-senha-smtp>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true # Ou outra config necessária

# Chave Secreta da API (a mesma que o cliente/frontend deve enviar no header 'minha-chave')
app.api-key=<sua-chave-secreta>

# E-mail de destino para onde as mensagens do formulário serão enviadas
app.email.to=<email-de-destino@exemplo.com>to