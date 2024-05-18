package com.web.springmvc.web_tin_tuc.service;

import com.web.springmvc.web_tin_tuc.dto.ForgotPasswordRequest;
import com.web.springmvc.web_tin_tuc.dto.PasswordResetRequest;
import com.web.springmvc.web_tin_tuc.model.ConfirmationToken;
import com.web.springmvc.web_tin_tuc.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserService userService;

    public void sendPasswordReset(ForgotPasswordRequest forgotPasswordRequest, Integer id) {
        userService.sendPasswordToken(forgotPasswordRequest, id);
    }

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        try {
            confirmationToken = confirmationTokenRepository.findByToken(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(confirmationToken.getConfirmedAt() != null) {
            confirmationTokenRepository.deleteByToken(token);
            throw new IllegalStateException("Link isn't valid!");
        }
        if(confirmationToken.getExpireAt().isBefore(LocalDateTime.now())) {
            confirmationTokenRepository.deleteByToken(token);
            throw new IllegalStateException("Token expired!");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
    }

    public void resetPassword(Integer id, PasswordResetRequest request) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByUserId(id);
        userService.verifyPasswordResetToken(confirmationToken, request);
    }
}
