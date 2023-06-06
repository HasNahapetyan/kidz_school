package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private SessionType sessionType;
    private boolean medicalAssistance;
    private String activity;
    private boolean transportation;
    private double price;
    private int meals;
}
