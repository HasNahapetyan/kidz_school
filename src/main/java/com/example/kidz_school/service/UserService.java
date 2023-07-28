package com.example.kidz_school.service;

import com.example.kidz_school.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.UUID;

public interface UserService {
    User findByEmail(String email);

    void sendPasswordResetEmail(String email);

    boolean verifyOTP(String email, String otp);

    void resetPassword(String email, String newPassword);

    User save(User user);

    void sendVerificationEmail(User user, UUID uuid);

    User findById(Long id);

    void editUserDetails(String firstName, String lastName, String email, Long currentUserId, User userByNewEmail, HttpSession session);

    boolean verifyEmail(UUID uuid, Model model);
}
