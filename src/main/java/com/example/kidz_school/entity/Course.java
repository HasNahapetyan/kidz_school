package com.example.kidz_school.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
