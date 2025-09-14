package com.javaMail.emailDemo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${app.api-key}")
    private String apiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestApiKey = request.getHeader("X-API-Key");
        if (requestApiKey != null && requestApiKey.equals(apiKey)) {
            return true;
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Api Key inválida.");

        return false;
    }
}
