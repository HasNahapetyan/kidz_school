package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "one_time_password")
public class OneTimePassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String otp;

    private LocalDateTime expiryDateTime;

    public OneTimePassword(String email, String otp, LocalDateTime expiryDateTime) {
        this.email = email;
        this.otp = otp;
        this.expiryDateTime = expiryDateTime;
    }
}
