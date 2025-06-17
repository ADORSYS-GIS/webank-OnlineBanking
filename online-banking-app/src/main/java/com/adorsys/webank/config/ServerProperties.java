package com.adorsys.webank.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for server-specific settings.
 */
@ConfigurationProperties(prefix = "server")
@Validated
public class ServerProperties {
    
    /**
     * Server private key in JWK format.
     * Should be provided via environment variable SERVER_PRIVATE_KEY_JSON.
     */
    @NotBlank(message = "Server private key must be provided")
    private String privateKey;
    
    /**
     * Server public key in JWK format.
     * Should be provided via environment variable SERVER_PUBLIC_KEY_JSON.
     */
    @NotBlank(message = "Server public key must be provided")
    private String publicKey;
    
    // Getters and Setters
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
