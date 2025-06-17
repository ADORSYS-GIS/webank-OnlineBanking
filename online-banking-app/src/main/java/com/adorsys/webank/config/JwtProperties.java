package com.adorsys.webank.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for JWT settings.
 */
@ConfigurationProperties(prefix = "jwt")
@Validated
public class JwtProperties {
    
    /**
     * JWT issuer name.
     */
    @NotBlank(message = "JWT issuer must be provided")
    private String issuer = "webank-online-banking";
    
    /**
     * JWT expiration time in milliseconds.
     */
    @Positive(message = "Expiration time must be a positive number")
    private long expirationTimeMs = 3600000; // 1 hour default
    
    // Getters and Setters
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public long getExpirationTimeMs() {
        return expirationTimeMs;
    }

    public void setExpirationTimeMs(long expirationTimeMs) {
        this.expirationTimeMs = expirationTimeMs;
    }
}
