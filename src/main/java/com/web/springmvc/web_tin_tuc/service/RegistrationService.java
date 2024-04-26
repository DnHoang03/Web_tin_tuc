package com.web.springmvc.web_tin_tuc.service;

import com.web.springmvc.web_tin_tuc.dto.RegistrationRequest;
import com.web.springmvc.web_tin_tuc.dto.UserDTO;
import com.web.springmvc.web_tin_tuc.model.ConfirmationToken;
import com.web.springmvc.web_tin_tuc.model.Role;
import com.web.springmvc.web_tin_tuc.model.User;
import com.web.springmvc.web_tin_tuc.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    public void register(RegistrationRequest request) {
        userService.register(new User(request.getUsername(), request.getEmail(), request.getPassword(), Role.USER));
    }

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        try {
            confirmationToken = confirmationTokenRepository.findByToken(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }
        if(confirmationToken.getExpireAt().isBefore(LocalDateTime.now())) {
            userService.deleteUserByEmail(confirmationToken.getUser().getEmail());
            confirmationTokenRepository.deleteByToken(token);
            throw new IllegalStateException("Token expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        userService.verifyToken(confirmationToken);
    }
}
