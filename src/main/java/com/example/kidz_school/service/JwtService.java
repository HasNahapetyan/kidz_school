package com.example.kidz_school.service;

import com.example.kidz_school.entity.User;

public interface JwtService {
    String generateToken(User user);
}