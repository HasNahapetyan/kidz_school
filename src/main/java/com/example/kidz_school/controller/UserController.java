package com.example.kidz_school.controller;

import com.example.kidz_school.dto.OTPRequestDTO;
import com.example.kidz_school.dto.SetNewPasswordRequestDTO;
import com.example.kidz_school.dto.UserRegistrationRequestDto;
import com.example.kidz_school.entity.ConfirmationToken;
import com.example.kidz_school.entity.Role;
import com.example.kidz_school.entity.User;
import com.example.kidz_school.mapper.UserMapper;
import com.example.kidz_school.service.ConfirmationTokenService;
import com.example.kidz_school.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping
    public String main() {
        return "sign-up-or-login";
    }

    @GetMapping("/forgot")
    public String showForgotPasswordForm() {
        return "sign-up-or-login";
    }

    @PostMapping("/forgot")
    public String processForgotPasswordForm(@RequestParam("email") String email) {
        try {
            userService.sendPasswordResetEmail(email);
            return "redirect:/sign-up-or-login/?forgot=true&email=" + email;
        } catch (NoSuchElementException e) {
            return "redirect:/sign-up-or-login/?forgot=false&email=" + email;
        }
    }

    @GetMapping("/verifyOTP")
    public String showOTPForm(Model model) {
        model.addAttribute("otpRequest", new OTPRequestDTO());
        return "sign-up-or-login";
    }

    @PostMapping("/verifyOTP")
    public String processOTPForm(@ModelAttribute("otpRequest") OTPRequestDTO otpRequest) {
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();
        if (userService.verifyOTP(email, otp)) {
            return "redirect:/sign-up-or-login/?verify=true&email=" + email;
        } else {
            return "redirect:/sign-up-or-login/?verify=false&email=" + email;
        }
    }

    @GetMapping("/reset")
    public String showResetPasswordForm(Model model) {
        model.addAttribute("passwordRequest", new SetNewPasswordRequestDTO());
        return "sign-up-or-login";
    }

    @PostMapping("/reset")
    public String processResetPasswordForm(@ModelAttribute("passwordRequest") SetNewPasswordRequestDTO passwordRequest) {
        String email = passwordRequest.getEmail();
        String newPassword = passwordRequest.getNewPassword();
        userService.resetPassword(email, newPassword);
        return "redirect:/sign-up-or-login/?reset=true&email=" + email;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserRegistrationRequestDto userRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "invalidUserInfo";
        }
        User userFromDB = userService.findByEmail(userRequest.getEmail());
        if (userFromDB == null) {
            User user = userMapper.mapFromRegisterDto(userRequest);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setRole(Role.USER);
            user.setEnabled(false);
            User savedUser = userService.save(user);

            UUID uuid = UUID.randomUUID();
            ConfirmationToken token = confirmationTokenService.generateConfirmationToken(uuid, savedUser);
            confirmationTokenService.save(token);
            userService.sendVerificationMassage(savedUser, uuid);
            return "welcome";
        }
        model.addAttribute("email", userRequest.getEmail());
        return "user-already-exists-page";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") UUID uuid, Model model) {
        ConfirmationToken token = confirmationTokenService.findByToken(uuid.toString());
        if (token == null) {
            model.addAttribute("message", " Invalid verification token");
            return "unauthorized";
        }
        User user = token.getUser();
        if (user == null) {
            model.addAttribute("message", " User not found");
            return "unauthorized";
        }
        if (user.isEnabled()) {
            model.addAttribute("message", " User is already enabled");
            return "unauthorized";
        }
        user.setEnabled(true);
        userService.save(user);
        return "authorized";
    }
}
