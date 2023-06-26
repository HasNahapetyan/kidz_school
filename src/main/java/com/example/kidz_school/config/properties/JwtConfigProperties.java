package com.example.kidz_school.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "platform.security.jwt")
public class JwtConfigProperties {

    private String issuer;
    private long expiration;
    private String secretKey;
}