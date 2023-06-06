package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private String createdBy;
    private String description;
    private String image;
    @Enumerated(EnumType.STRING)
    private Category category;
}
