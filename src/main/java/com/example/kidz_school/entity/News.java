package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private UUID id;
    private String title;
    private LocalDateTime createdAt;
    private String createdBy;
    private String description;
    private String image;
    @Enumerated
    private Category category;

}
