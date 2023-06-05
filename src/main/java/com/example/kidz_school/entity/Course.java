package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String title;
    private double price;
    private String description;
    private LocalDate startDate;
    private int minEnrollmentAge;
    private int maxEnrollmentAge;
    private int availableSeats;
    private int courseDuration;
    @OneToMany
    private List<CourseSchedule> courseSchedules;

}
