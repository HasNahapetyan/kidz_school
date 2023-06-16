package com.example.kidz_school.service;

public interface UserService {
    void sendPasswordResetEmail(String email);

    boolean verifyOTP(String email, String otp);

    void resetPassword(String email, String newPassword);
}
