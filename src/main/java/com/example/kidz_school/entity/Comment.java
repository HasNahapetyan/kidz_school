package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private UUID id;
    @ManyToOne
    private News news;
    @ManyToOne
    private User user;
    private String message;
    private LocalDateTime createdAt;
    @OneToMany
    private List<Reply> replies;

}
