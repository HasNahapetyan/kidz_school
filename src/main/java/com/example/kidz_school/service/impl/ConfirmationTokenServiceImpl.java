package com.example.kidz_school.service.impl;

import com.example.kidz_school.entity.ConfirmationToken;
import com.example.kidz_school.entity.User;
import com.example.kidz_school.mapper.ConfirmationTokenMapper;
import com.example.kidz_school.repository.ConfirmationTokenRepository;
import com.example.kidz_school.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ConfirmationTokenMapper confirmationTokenMapper;

    @Value("${platform.security.confirmation-token.expiration}")
    private long expiration;

    @Override
    public ConfirmationToken save(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public ConfirmationToken findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public ConfirmationToken generateConfirmationToken(UUID uuid, User savedUser) {
        ConfirmationToken confirmationToken = confirmationTokenMapper.toEntity(uuid.toString());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(expiration));
        confirmationToken.setToken(uuid.toString());
        confirmationToken.setUser(savedUser);
        return confirmationToken;
    }

    @Transactional
    @Scheduled(cron = "0 */20 * * * *") //every 20 minutes
    public void deleteExpiredTokens() {
        confirmationTokenRepository.removeByExpiresAtBefore(LocalDateTime.now());
    }

}

