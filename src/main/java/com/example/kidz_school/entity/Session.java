package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    @Enumerated
    private SessionType sessionType;
    private boolean medicalAssistance;
    private String activity;
    private boolean transportation;
    private double price;
    private int meals;

}
