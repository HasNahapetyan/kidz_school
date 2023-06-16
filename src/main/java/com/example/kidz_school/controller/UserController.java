package com.example.kidz_school.controller;

import com.example.kidz_school.dto.OTPRequestDTO;
import com.example.kidz_school.dto.SetNewPasswordRequestDTO;
import com.example.kidz_school.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
@RequestMapping("/sign-up-or-login")
public class UserController {
    private final UserService userService;

    @GetMapping("/")
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
}
