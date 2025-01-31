package com.adorsys.webank.obs.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jwt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;

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
    public boolean validateJWT(String jwtToken) {
        try {
            SignedJWT signedJWT = parseJWT(jwtToken);
            String phoneNumberCert = extractPhoneNumberCert(signedJWT);
            SignedJWT phoneNumberCertJwt = parseJWT(phoneNumberCert);
            ECKey publicKey = loadPublicKey();

            return verifySignature(phoneNumberCertJwt, publicKey);
        } catch (Exception e) {
            logger.error("Error during JWT validation: ", e);
            return false;
        }
    }

    private SignedJWT parseJWT(String jwt) throws ParseException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        logger.info("Parsed JWT: {}", signedJWT);
        return signedJWT;
    }

    private String extractPhoneNumberCert(SignedJWT signedJWT) {
        Object phoneNumberCertObj = signedJWT.getHeader().toJSONObject().get("phoneNumberJwt");
        if (phoneNumberCertObj == null) {
            throw new IllegalArgumentException("Missing phoneNumberCert in JWT header.");
        }
        String phoneNumberCert = phoneNumberCertObj.toString();
        logger.info("Extracted phoneNumberCert: {}", phoneNumberCert);
        return phoneNumberCert;
    }

    private ECKey loadPublicKey() throws ParseException {
        if (SERVER_PUBLIC_KEY_JSON == null || SERVER_PUBLIC_KEY_JSON.isEmpty()) {
            throw new IllegalStateException("Public key JSON is not configured properly.");
        }
        JWK jwk = JWK.parse(SERVER_PUBLIC_KEY_JSON);
        logger.info("Loaded JWK from backend: {}", jwk);

        if (!(jwk instanceof ECKey publicKey) || jwk.isPrivate()) {
            throw new IllegalArgumentException("Invalid JWK provided by backend.");
        }
        return publicKey;
    }

    private boolean verifySignature(SignedJWT phoneNumberCertJwt, ECKey publicKey) throws JOSEException {
        JWSVerifier phoneNumberCertVerifier = new ECDSAVerifier(publicKey);
        boolean isValid = phoneNumberCertJwt.verify(phoneNumberCertVerifier);

        if (!isValid) {
            logger.error("phoneNumberCert signature validation failed.");
        } else {
            logger.info("JWT validation successful.");
        }
        return isValid;
    }
}
