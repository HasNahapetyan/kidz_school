package com.example.kidz_school.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationRequestDto {
    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 15, message = "First name must be between 3 and 15 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 15, message = "Last name must be between 3 and 15 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+\\d{1,3}( )\\d{2}( )\\d{6}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=()])(?=.*[a-zA-Z0-9]).{6,}$",
            message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character")
    private final String password;

    @NotBlank(message = "RePassword is required")
    @Size(min = 6, message = "RePassword must be at least 6 characters long")
    private final String rePassword;

    @AssertTrue(message = "Password and RePassword must match")
    public boolean isPasswordMatch() {
        return password.equals(rePassword);
    }
}
