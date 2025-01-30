package com.adorsys.webank.obs.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jwt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class JwtCertValidator {
    private static final Logger logger = LoggerFactory.getLogger(JwtCertValidator.class);

    public static boolean validateJWT(String jwtToken) {
        try {
            SignedJWT signedJWT = parseJWT(jwtToken);
            logger.info("jwt is : {}", jwtToken);

            SignedJWT phoneJwtSigned = extractPhoneJwt(signedJWT);
            logger.info("phonejwtsigned : {} ", phoneJwtSigned);

            if (!verifySignature(phoneJwtSigned)) {
                logger.error("phoneNumberJwt signature validation failed.");
                return false;
            }

            if (!verifySignature(signedJWT)) {
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

    private static SignedJWT parseJWT(String jwtToken) throws ParseException {
        return SignedJWT.parse(jwtToken);
    }

    private static SignedJWT extractPhoneJwt(SignedJWT signedJWT) throws ParseException {
        Object phoneJwtObj = signedJWT.getHeader().toJSONObject().get("phoneNumberJwt");
        if (phoneJwtObj == null) {
            throw new IllegalArgumentException("Missing 'phoneNumberJwt' field in JWT header.");
        }
        return SignedJWT.parse(phoneJwtObj.toString());
    }

    private static boolean verifySignature(SignedJWT signedJWT) throws JOSEException, ParseException {
        JWK rawJwk = signedJWT.getHeader().getJWK();
        if (rawJwk == null) {
            throw new IllegalArgumentException("Missing JWK in JWT header.");
        }
        JWK jwk = JWK.parse(rawJwk.toJSONObject());

        if (!(jwk instanceof ECKey publicKey)) {
            throw new IllegalArgumentException("Invalid or missing ECKey in JWT.");
        }

        JWSVerifier verifier = new ECDSAVerifier(publicKey);
        return signedJWT.verify(verifier);
    }

}
