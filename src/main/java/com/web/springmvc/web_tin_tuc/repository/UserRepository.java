package com.web.springmvc.web_tin_tuc.repository;

import com.web.springmvc.web_tin_tuc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT c FROM User c WHERE c.username = :username AND c.status = 1 AND c.isEnabled = true ")
    User findByUsername(String username);
    boolean existsByUsername(String username);

    User findFirstByUsername(String username);
    @Query("SELECT c FROM User c WHERE c.email = :email AND c.status = 1 ")
    User findByEmail(String email);
    boolean existsByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

    @Query("SELECT c FROM User c WHERE c.role = 'ADMIN' ")
    User findByRole();
}
