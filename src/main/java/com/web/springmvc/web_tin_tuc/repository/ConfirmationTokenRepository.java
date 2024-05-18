package com.web.springmvc.web_tin_tuc.repository;

import com.web.springmvc.web_tin_tuc.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByToken(String token);

    ConfirmationToken findByUserId(Integer id);
    @Transactional
    void deleteByToken(String token);
}
