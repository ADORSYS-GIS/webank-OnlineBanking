package com.adorsys.webank.obs.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jwt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtCertValidator {
    private static final Logger logger = LoggerFactory.getLogger(JwtCertValidator.class);

    /**
     * Validates the JWT by extracting the phoneJwt from its header and verifying signatures.
     *
     * @param jwtToken The JWT token string to validate.
     * @return True if valid, false otherwise.
     */
    public static boolean validateJWT(String jwtToken) {
        try {
            // Parse the main JWT token coming from the frontend
            SignedJWT signedJWT = SignedJWT.parse(jwtToken);

            logger.info("jwt is : {}", jwtToken);

            // Extract "phoneNumberJwt" from the JWT header
            Object phoneJwtObj = signedJWT.getHeader().toJSONObject().get("phoneNumberJwt");
            if (phoneJwtObj == null) {
                throw new IllegalArgumentException("Missing 'phoneNumberJwt' field in JWT header.");
            }

            // Parse phoneJwt as a separate JWT
            SignedJWT phoneJwtSigned = SignedJWT.parse(phoneJwtObj.toString());

            logger.info("phonejwtsigned : {} ", phoneJwtSigned);

            // Extract JWK from phoneJwt header
            JWK rawJwk = phoneJwtSigned.getHeader().getJWK();
            if (rawJwk == null) {
                throw new IllegalArgumentException("Missing JWK in phoneNumberJwt header.");
            }
            JWK jwk = JWK.parse(rawJwk.toJSONObject());

            // Validate the JWK for phoneJwt
            if (!(jwk instanceof ECKey publicKeyPhone)) {
                throw new IllegalArgumentException("Invalid or missing ECKey in phoneNumberJwt.");
            }

            // Use the extracted key to verify the phoneJwt signature
            JWSVerifier phoneJwtVerifier = new ECDSAVerifier(publicKeyPhone);

            if (!phoneJwtSigned.verify(phoneJwtVerifier)) {
                logger.error("phoneNumberJwt signature validation failed.");
                return false;
            }

            // Extract JWK from the main signedJWT header
            JWK rawJwkMain = signedJWT.getHeader().getJWK();
            if (rawJwkMain == null) {
                throw new IllegalArgumentException("Missing JWK in signedJWT header.");
            }
            JWK jwkMain = JWK.parse(rawJwkMain.toJSONObject());

            // Validate the JWK for signedJWT
            if (!(jwkMain instanceof ECKey publicKeyMain)) {
                throw new IllegalArgumentException("Invalid or missing ECKey in signedJWT.");
            }

            // Use the extracted key to verify the main signedJWT signature
            JWSVerifier signedJWTVerifier = new ECDSAVerifier(publicKeyMain);

            if (!signedJWT.verify(signedJWTVerifier)) {
                logger.error("JWT signature validation failed.");
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
