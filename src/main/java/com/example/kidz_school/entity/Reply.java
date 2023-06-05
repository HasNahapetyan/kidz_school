package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity

public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private UUID id;
    @ManyToOne
    private User user;
    private String message;
    private LocalDateTime createdAt;
    @ManyToOne
    private Comment parentComment;

}
