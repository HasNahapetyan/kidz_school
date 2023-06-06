package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private News news;
    @ManyToOne
    private User user;
    private String message;
    private LocalDateTime createdAt;
    @OneToMany
    private List<Reply> replies;
}
