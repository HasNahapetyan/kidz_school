package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private UUID id;

    private String token;
    @OneToOne
    private User user;

    private LocalDateTime expiresAt;
}
