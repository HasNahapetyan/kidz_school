package com.example.kidz_school.dto;

import lombok.Data;

@Data
public class SetNewPasswordRequestDTO {
    private String email;
    private String newPassword;
}
