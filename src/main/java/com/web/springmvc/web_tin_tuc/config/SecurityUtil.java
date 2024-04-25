package com.web.springmvc.web_tin_tuc.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            // Neu chua dang nhap, anonymous se duoc spring security dung de dai dien cho nguoi dung
            // Neu nguoi dung da dang nhap
            String currentUsername = authentication.getName();
            return currentUsername;
        }
        return null;
    }
}
