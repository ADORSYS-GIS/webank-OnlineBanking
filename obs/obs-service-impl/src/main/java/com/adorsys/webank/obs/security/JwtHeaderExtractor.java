package com.adorsys.webank.obs.security;

import com.nimbusds.jwt.*;
import org.slf4j.*;

import java.text.*;

public class JwtHeaderExtractor {

    private static final Logger logger = LoggerFactory.getLogger(JwtHeaderExtractor.class);

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

        logger.info("JWT FROM HEADER: {}", jwtToken);
        if (jwtToken == null || jwtToken.isEmpty()) {
            logger.error("JWT token is null or empty.");
            throw new IllegalArgumentException("JWT token is required.");
        }
        logger.debug("Attempting to extract field '{}' from JWT header.", fieldName);

        try {
            // Parse the JWT
            SignedJWT signedJWT = SignedJWT.parse(jwtToken);
            logger.debug("Successfully parsed JWT: {}", signedJWT);

            // Extract the field from the header
            Object fieldValue = signedJWT.getHeader().toJSONObject().get(fieldName);
            if (fieldValue != null) {
                String value = fieldValue.toString();
                logger.info("Successfully extracted {} from JWT header: {}", fieldName, value);
                return value;
            }

            logger.warn("Missing {} in JWT header.", fieldName);
            return null;
        } catch (ParseException e) {
            logger.error("Failed to parse JWT token while extracting field '{}': {}", fieldName, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to parse JWT token.", e);
        }
    }
}