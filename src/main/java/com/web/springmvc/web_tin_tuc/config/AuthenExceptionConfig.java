package com.web.springmvc.web_tin_tuc.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenExceptionConfig implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String redirectUrl = determineRedirectUrl(exception);
        response.sendRedirect(redirectUrl);
    }

    private String determineRedirectUrl(AuthenticationException exception) {
        return "/auth/login?error=true";
    }
}