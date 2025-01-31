package com.adorsys.webank.obs.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jwt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtCertValidator {
    private static final Logger logger = LoggerFactory.getLogger(JwtCertValidator.class);

    @Value("${server.public.key.json}")
    private String SERVER_PUBLIC_KEY_JSON;

    /**
     * Validates the JWT by extracting the phoneNumberCert from its header and verifying signatures.
     *
     * @param jwtToken The JWT token string to validate.
     * @return True if valid, false otherwise.
     */
    public boolean validateJWT(String jwtToken) { // Converted to instance method
        try {
            // Parse the main JWT token
            SignedJWT signedJWT = SignedJWT.parse(jwtToken);
            logger.info("Parsed JWT: {}", signedJWT);

            // Extract "phoneNumberCert" from the JWT header
            Object phoneNumberCertObj = signedJWT.getHeader().toJSONObject().get("phoneNumberJwt");
            if (phoneNumberCertObj == null) {
                throw new IllegalArgumentException("Missing phoneNumberCert in JWT header.");
            }
            String phoneNumberCert = phoneNumberCertObj.toString();
            logger.info("Extracted phoneNumberCert: {}", phoneNumberCert);

            // Parse the phoneNumberCert (another JWT)
            SignedJWT phoneNumberCertJwt = SignedJWT.parse(phoneNumberCert);
            logger.info("Parsed phoneNumberCert JWT: {}", phoneNumberCertJwt);

            // Ensure the public key JSON is not null
            if (SERVER_PUBLIC_KEY_JSON == null || SERVER_PUBLIC_KEY_JSON.isEmpty()) {
                throw new IllegalStateException("Public key JSON is not configured properly.");
            }

            // Load public key from configuration
            JWK jwk = JWK.parse(SERVER_PUBLIC_KEY_JSON);
            logger.info("Loaded JWK from backend: {}", jwk);

            // Validate the phoneNumberCert signature using the backend-provided JWK
            if (!(jwk instanceof ECKey publicKey) || jwk.isPrivate()) {
                throw new IllegalArgumentException("Invalid JWK provided by backend.");
            }

            JWSVerifier phoneNumberCertVerifier = new ECDSAVerifier(publicKey);
            if (!phoneNumberCertJwt.verify(phoneNumberCertVerifier)) {
                logger.error("phoneNumberCert signature validation failed.");
                return false;
            }

            logger.info("JWT validation successful.");
            return true;
        } catch (Exception e) {
            logger.error("Error during JWT validation: ", e);
            return false;
        }
    }
}
