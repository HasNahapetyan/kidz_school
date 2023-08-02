package com.example.kidz_school.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditUserDto {
    @Size(max = 15, message = "First name must be between 3 and 10 characters")
    private String firstName;

    @Size(max = 15, message = "Last name must be between 3 and 10 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;
}
