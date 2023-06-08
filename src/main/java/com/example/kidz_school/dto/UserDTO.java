package com.example.kidz_school.dto;

import com.example.kidz_school.entity.Role;
import lombok.Data;
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private boolean enabled;
    private String hashedPassword;
}
