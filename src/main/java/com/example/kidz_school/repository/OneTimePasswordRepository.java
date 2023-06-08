package com.example.kidz_school.repository;

import com.example.kidz_school.entity.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {
    OneTimePassword findByEmailAndOtp(String email, String otp);

    void deleteByEmailEqualsIgnoreCase(String email);

    void removeAllByExpiryDateTimeBefore(LocalDateTime dateTime);
}
