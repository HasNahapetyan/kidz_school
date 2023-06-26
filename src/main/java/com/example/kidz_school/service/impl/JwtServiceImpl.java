package com.example.kidz_school.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.kidz_school.config.properties.JwtConfigProperties;
import com.example.kidz_school.entity.User;
import com.example.kidz_school.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final String USER_ID = "userId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE = "phone";
    private static final String ROLE = "role";
    private static final String AUTHORITIES = "authorities";

    private final JwtConfigProperties jwtConfigProperties;
    private static final String ENABLED = "enabled";

    public String generateToken(User user) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfigProperties.getSecretKey());
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim(USER_ID, user.getId())
                .withClaim(FIRST_NAME, user.getFirstName())
                .withClaim(LAST_NAME, user.getLastName())
                .withClaim(PHONE, user.getPhone())
                .withClaim(ROLE, user.getRole().toString())
                .withClaim(ENABLED, user.isEnabled())
                .withClaim(AUTHORITIES, authoritiesAsString(user.getRole().getAuthorities()))
                .withIssuer(jwtConfigProperties.getIssuer())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(jwtConfigProperties.getExpiration(), ChronoUnit.MINUTES))
                .sign(Algorithm.HMAC256(keyBytes));
    }

    public List<String> authoritiesAsString(List<SimpleGrantedAuthority> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::getAuthority).toList();
    }

    public List<SimpleGrantedAuthority> authoritiesFromString(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }

    public JWTVerifier jwtVerifier() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfigProperties.getSecretKey());
        return JWT.require(Algorithm.HMAC256(keyBytes)).build();
    }
}