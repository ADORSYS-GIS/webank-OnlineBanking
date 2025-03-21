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
    private String serverPublicKeyJson;
    /**
     * Validates the JWT by extracting either accountJwt or phoneNumberJwt from its header and verifying signatures.
     *
     * @param jwtToken The JWT token string to validate.
     * @return True if valid, false otherwise.
     */
    public boolean validateJWT(String jwtToken) {
        try {
            SignedJWT signedJWT = parseJWT(jwtToken);
            String cert = extractCert(signedJWT);
            SignedJWT certJwt = parseJWT(cert);
            ECKey publicKey = loadPublicKey();

            // Validate the accountCert
            return verifySignature(certJwt, publicKey);
        } catch (Exception e) {
            logger.error("Error during JWT validation: ", e);
            return false;
        }
    }

    private SignedJWT parseJWT(String jwt) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        logger.info("Parsed JWT: {}", signedJWT);
        return signedJWT;
    }

    private String extractCert(SignedJWT signedJWT) {
        // Check for accountJwt or phoneNumberJwt field in the JWT header
        Object accountJwtObj = signedJWT.getHeader().toJSONObject().get("accountJwt");
        if (accountJwtObj != null) {
            String accountJwt = accountJwtObj.toString();
            logger.info("Extracted accountJwt: {}", accountJwt);
            return accountJwt;
        }

        Object kycJwtObj = signedJWT.getHeader().toJSONObject().get("kycJwt");
        if (kycJwtObj != null) {
            String kycJwt = kycJwtObj.toString();
            logger.info("Extracted kycJwt: {}", kycJwt);
            return kycJwt;
        }

        Object phoneNumberCertObj = signedJWT.getHeader().toJSONObject().get("phoneNumberJwt");
        if (phoneNumberCertObj != null) {
            String phoneNumberCert = phoneNumberCertObj.toString();
            logger.info("Extracted phoneNumberJwt: {}", phoneNumberCert);
            return phoneNumberCert;
        }

        throw new IllegalArgumentException("Missing either accountJwt or phoneNumberJwt in JWT header.");
    }

    private ECKey loadPublicKey() throws ParseException {
        if (serverPublicKeyJson == null || serverPublicKeyJson.isEmpty()) {
            throw new IllegalStateException("Public key JSON is not configured properly.");
        }
        JWK jwk = JWK.parse(serverPublicKeyJson);
        logger.info("Loaded JWK from backend: {}", jwk);

        if (!(jwk instanceof ECKey publicKey) || jwk.isPrivate()) {
            throw new IllegalArgumentException("Invalid JWK provided by backend.");
        }
        return publicKey;
    }

    private boolean verifySignature(SignedJWT certJwt, ECKey publicKey) throws JOSEException {
        JWSVerifier certVerifier = new ECDSAVerifier(publicKey);
        boolean isValid = certJwt.verify(certVerifier);

        if (!isValid) {
            logger.error("JWT signature validation failed.");
        } else {
            logger.info("JWT validation successful.");
        }
        return isValid;
    }
}
