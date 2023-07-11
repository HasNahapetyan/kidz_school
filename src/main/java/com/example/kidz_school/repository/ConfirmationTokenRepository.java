package com.example.kidz_school.repository;

import com.example.kidz_school.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {

    ConfirmationToken findByToken(String string);

    void removeByExpiresAtBefore(LocalDateTime localDateTime);

    ConfirmationToken save(ConfirmationToken confirmationToken);

}
