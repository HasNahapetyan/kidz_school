package com.example.kidz_school.service;

import com.example.kidz_school.entity.ConfirmationToken;
import com.example.kidz_school.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ConfirmationTokenService {

    ConfirmationToken save(ConfirmationToken token);

    ConfirmationToken findByToken(String token);

    ConfirmationToken generateConfirmationToken(UUID uuid, User savedUser);

}
