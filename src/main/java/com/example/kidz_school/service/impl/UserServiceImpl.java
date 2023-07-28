package com.example.kidz_school.service.impl;

import com.example.kidz_school.entity.ConfirmationToken;
import com.example.kidz_school.entity.OneTimePassword;
import com.example.kidz_school.entity.User;
import com.example.kidz_school.repository.OneTimePasswordRepository;
import com.example.kidz_school.repository.UserRepository;
import com.example.kidz_school.service.ConfirmationTokenService;
import com.example.kidz_school.service.EmailService;
import com.example.kidz_school.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OneTimePasswordRepository oneTimePasswordRepository;
    @Value("${site.url}")
    private String siteUrl;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtServiceImpl jwtService;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        boolean userExists = userRepository.existsByEmail(email);
        if (!userExists) {
            throw new NoSuchElementException("User not found");
        }
        String otp = generateOtp();
        LocalDateTime expiryDateTime = LocalDateTime.now().plusHours(2);
        OneTimePassword otpEntity = new OneTimePassword(email, otp, expiryDateTime);
        oneTimePasswordRepository.save(otpEntity);

        String subject = "Password Reset";
        String body = "Your OTP for password reset is: " + otp;
        emailService.sendEmail(email, subject, body);
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        OneTimePassword otpEntity = oneTimePasswordRepository.findByEmailAndOtp(email, otp);
        if (otpEntity == null || otpEntity.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        return otpEntity.getOtp().equals(otp);
    }

    @Override
    @Transactional
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }
        user.setPassword(newPassword);
        oneTimePasswordRepository.deleteByEmailEqualsIgnoreCase(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void sendVerificationEmail(User user, UUID uuid) {
        String messageBody = new StringBuilder().append("Hi ")
                .append(user.getFirstName())
                .append("\n Please verify your email by clicking on this url: ")
                .append(siteUrl)
                .append("/user/verify?token=")
                .append(uuid).toString();
        emailService.sendEmail(user.getEmail(), "Welcome", messageBody);
    }

    @Override
    public void editUserDetails(String firstName, String lastName, String email, Long currentUserId, User userByNewEmail, HttpSession session) {
        User user = findById(currentUserId);
        if (!firstName.equals("")) {
            user.setFirstName(firstName);
        }
        if (!lastName.equals("")) {
            user.setLastName(lastName);
        }
        changeEmail(user, email, userByNewEmail);
        //reset Authorization token
        String token = jwtService.generateToken(user);
        session.setAttribute("AuthorizationToken", "Bearer " + token);
        save(user);
    }

    // Verify email for registration and email change
    @Override
    public boolean verifyEmail(UUID uuid, Model model) {
        ConfirmationToken token = confirmationTokenService.findByToken(uuid.toString());
        if (token == null) {
            model.addAttribute("message", " Invalid verification token");
            return false;
        }
        User user = token.getUser();
        if (user == null) {
            model.addAttribute("message", " User not found");
            return false;
        }
        if (user.isEnabled()) {
            model.addAttribute("message", " User is already enabled");
            return false;
        }
        user.setEnabled(true);
        save(user);
        return true;
    }

    // Change email and send  email
    private void changeEmail(User user, String newEmail, User userByNewEmail) {
        if (!newEmail.equals("") && userByNewEmail == null) {
            user.setEmail(newEmail);
            user.setEnabled(false);
            //send verification email
            UUID uuid = UUID.randomUUID();
            ConfirmationToken token = confirmationTokenService.generateConfirmationToken(uuid, user);
            confirmationTokenService.save(token);
            String messageBody = new StringBuilder().append("Hi ")
                    .append(user.getFirstName())
                    .append("\n Please verify your email by clicking on this url: ")
                    .append(siteUrl)
                    .append("/user/verifyEmailChange?token=")
                    .append(uuid).toString();
            emailService.sendEmail(newEmail, "Email Change Verification", messageBody);
            userRepository.save(user);
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    private String generateOtp() {
        String otpChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int otpLength = 6;
        StringBuilder otpBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < otpLength; i++) {
            int randomIndex = random.nextInt(otpChars.length());
            char otpChar = otpChars.charAt(randomIndex);
            otpBuilder.append(otpChar);
        }
        return otpBuilder.toString();
    }

    @Transactional
    @Scheduled(cron = "0 0/30 * * * *")
    public void deleteExpiredOtps() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        oneTimePasswordRepository.removeAllByExpiryDateTimeBefore(currentDateTime);
    }
}

