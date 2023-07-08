package com.example.kidz_school;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
public class KidzSchoolApplication {
    public static void main(String[] args) {
        SpringApplication.run(KidzSchoolApplication.class, args);
    }
}
