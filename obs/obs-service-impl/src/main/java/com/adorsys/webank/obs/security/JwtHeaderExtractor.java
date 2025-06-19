package com.adorsys.webank.obs.security;

import com.nimbusds.jwt.*;

import lombok.extern.slf4j.Slf4j;


import java.text.*;
@Slf4j
public class JwtHeaderExtractor {

   

    // Private constructor to prevent instantiation
    private JwtHeaderExtractor() {

    }

    /**
     * Extracts a specific field (e.g., accountJwt, kycCertJwt) from the JWT header.
     *
     * @param jwtToken The JWT token string.
     * @param fieldName The name of the field to extract (e.g., "accountJwt", "kycCertJwt").
     * @return The value of the requested field, or null if not present.
     */
    public static String extractField(String jwtToken, String fieldName) {

        log.info("JWT FROM HEADER: {}", jwtToken);
        if (jwtToken == null || jwtToken.isEmpty()) {
            log.error("JWT token is null or empty.");
            throw new IllegalArgumentException("JWT token is required.");
        }
        log.debug("Attempting to extract field '{}' from JWT header.", fieldName);

        try {
            // Parse the JWT
            SignedJWT signedJWT = SignedJWT.parse(jwtToken);
            log.debug("Successfully parsed JWT: {}", signedJWT);

            // Extract the field from the header
            Object fieldValue = signedJWT.getHeader().toJSONObject().get(fieldName);
            if (fieldValue != null) {
                String value = fieldValue.toString();
                log.info("Successfully extracted {} from JWT header: {}", fieldName, value);
                return value;
            }

            log.warn("Missing {} in JWT header.", fieldName);
            return null;
        } catch (ParseException e) {
            log.error("Failed to parse JWT token while extracting field '{}': {}", fieldName, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to parse JWT token.", e);
        }
    }
}