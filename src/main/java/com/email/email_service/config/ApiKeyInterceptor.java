package com.email.email_service.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${my-key-mega-power-secret}")
    private String apiKey;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestApiKey = request.getHeader("minha-chave");
        if (requestApiKey != null && requestApiKey.equals(apiKey)) {
            return true;
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "chabve de API inválida!");
        return false;
    }
}
