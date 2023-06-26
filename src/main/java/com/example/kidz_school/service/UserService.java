package com.example.kidz_school.service;

import com.example.kidz_school.entity.User;

import java.util.Optional;

public interface UserService {
    User findByEmail(String email);
    void sendPasswordResetEmail(String email);

    boolean verifyOTP(String email, String otp);

    void resetPassword(String email, String newPassword);
}
