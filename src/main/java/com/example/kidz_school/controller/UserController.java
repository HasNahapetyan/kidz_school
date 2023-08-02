package com.example.kidz_school.controller;

import com.example.kidz_school.dto.EditUserDto;
import com.example.kidz_school.dto.OTPRequestDTO;
import com.example.kidz_school.dto.SetNewPasswordRequestDTO;
import com.example.kidz_school.dto.UserRegistrationRequestDto;
import com.example.kidz_school.entity.ConfirmationToken;
import com.example.kidz_school.entity.Role;
import com.example.kidz_school.entity.User;
import com.example.kidz_school.mapper.UserMapper;
import com.example.kidz_school.security.CurrentUser;
import com.example.kidz_school.service.ConfirmationTokenService;
import com.example.kidz_school.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
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
            return "redirect:/user?forgot=true&email=" + email;
        } catch (NoSuchElementException e) {
            return "redirect:/user?forgot=false&email=" + email;
        }
    }

    @GetMapping("/verifyOTP")
    public String showOTPForm(Model model) {
        model.addAttribute("otpRequest", new OTPRequestDTO());
        return "sign-up-or-login";
    }

    @PostMapping("/verifyOTP")
    public String processOTPForm(@Valid @ModelAttribute("otpRequest") OTPRequestDTO otpRequest) {
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();
        if (userService.verifyOTP(email, otp)) {
            return "redirect:/user?verify=true&email=" + email;
        } else {
            return "redirect:/user?verify=false&email=" + email;
        }
    }

    @GetMapping("/reset")
    public String showResetPasswordForm(Model model) {
        model.addAttribute("passwordRequest", new SetNewPasswordRequestDTO());
        return "sign-up-or-login";
    }

    @PostMapping("/reset")
    public String processResetPasswordForm(@Valid @ModelAttribute("passwordRequest") SetNewPasswordRequestDTO passwordRequest) {
        String email = passwordRequest.getEmail();
        String newPassword = passwordRequest.getNewPassword();
        userService.resetPassword(email, passwordEncoder.encode(newPassword));
        return "redirect:/user?reset=true&email=" + email;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserRegistrationRequestDto userRequest, BindingResult bindingResult, Model model) {
        log.info("User attempt to register with email: {}", userRequest.getEmail());
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
            userService.sendVerificationEmail(savedUser, uuid);
            return "welcome";
        }
        model.addAttribute("email", userRequest.getEmail());
        return "user-already-exists-page";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") UUID uuid, Model model) {
        if (userService.verifyEmail(uuid, model))
            return "authorized";
        return "unauthorized";
    }

    @PostMapping("/edit")
    public String editDetails(@Valid @ModelAttribute EditUserDto editUserDto,
                              Model model,
                              BindingResult bindingResult,
                              HttpSession session) {
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = currentUser.getId();
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "invalidUserInfo";
        }
        String newEmail = editUserDto.getEmail();
        String newFirstName = editUserDto.getFirstName();
        String newLastName = editUserDto.getLastName();
        // Perform validation to check at least one field contains a value
        if (newFirstName.equals("") && newLastName.equals("") && newEmail.equals("")) {
            model.addAttribute("message", "At least one field must contain a value.");
            return "invalidUserInfo";
        }
        //check email conflict
        User userByNewEmail = userService.findByEmail(newEmail);
        if (userByNewEmail != null && !Objects.equals(userByNewEmail.getId(), currentUserId)) {
            model.addAttribute("email", newEmail);
            return "user-already-exists-page";
        }
        userService.editUserDetails(newFirstName, newLastName, newEmail, currentUserId, userByNewEmail, session);
        return "redirect:../";
    }


    // Endpoint to handle email verification for email change
    @GetMapping("/verifyEmailChange")
    public String verifyEmailChange(@RequestParam("token") UUID uuid, Model model) {
        if (userService.verifyEmail(uuid, model)) {
            return "email-change-successful";
        }
        return "unauthorized";
    }

}
