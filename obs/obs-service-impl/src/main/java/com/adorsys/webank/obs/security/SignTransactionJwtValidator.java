package com.adorsys.webank.obs.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.*;
import com.nimbusds.jwt.proc.BadJWTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class SignTransactionJwtValidator {
    private static final Logger logger = LoggerFactory.getLogger(SignTransactionJwtValidator.class);

    @Value("${server.public.key.json}")
    String serverPublicKeyJson;
    /**
     * Validates the transaction JWT by extracting it from the main JWT header,
     * then verifying its signature using its own public key.
     * Additionally, verifies the accountCert using the application's public key.
     *
     * @param jwtToken The main JWT token string to validate.
     * @return True if valid, false otherwise.
     */
    public boolean validateSignTransactionJWT(String jwtToken) {
        logger.info("Starting validation for JWT: {}", jwtToken);
        try {
            // Parse the main JWT
            SignedJWT mainJWT = parseJWT(jwtToken);

            // Extract `transactionJwt` from the header
            String transactionJwtString = extractTransactionJwt(mainJWT);
            SignedJWT transactionJWT = parseJWT(transactionJwtString);
            JWSObject jwsObject = JWSObject.parse(transactionJwtString);

            // Extract public key
            logger.info("Extracting and validating JWK...");
            JWK transactionPublicKey = extractAndValidateJWK(jwsObject);
            logger.info("Public key extracted and validated: {}", transactionPublicKey);

            // Verify the transaction JWT signature using its public key
            if (!verifySignature(transactionJWT, (ECKey) transactionPublicKey)) {
                logger.warn("Transaction JWT signature verification failed.");
                return false;
            }
            logger.info("Transaction JWT signature verified successfully.");

            // Extract and validate `accountJwt` from the transaction JWT header
            String accountCert = extractAccountCert(transactionJWT);
            SignedJWT accountCertJwt = parseJWT(accountCert);
            ECKey appPublicKey = loadPublicKey();

            // Verify the account certificate signature
            if (!verifySignature(accountCertJwt, appPublicKey)) {
                logger.warn("Account certificate signature verification failed.");
                return false;
            }
            logger.info("Account certificate signature verified successfully.");

            return true;
        } catch (Exception e) {
            logger.error("Error during transaction JWT validation: ", e);
            return false;
        }
    }

    private SignedJWT parseJWT(String jwt) throws ParseException {
        logger.info("Parsing JWT: {}", jwt);
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        logger.info("Parsed JWT: {}", signedJWT);
        return signedJWT;
    }

    private String extractTransactionJwt(SignedJWT signedJWT) {
        logger.info("Extracting transactionJwt from JWT header...");
        Object transactionJwtObj = signedJWT.getHeader().toJSONObject().get("transactionJwt");
        if (transactionJwtObj == null) {
            throw new IllegalArgumentException("Missing transactionJwt in JWT header.");
        }
        String transactionJwt = transactionJwtObj.toString();
        logger.info("Extracted transactionJwt: {}", transactionJwt);
        return transactionJwt;
    }

    private static JWK extractAndValidateJWK(JWSObject jwsObject)
            throws BadJOSEException, JsonProcessingException, ParseException {
        logger.info("Extracting JWK from JWT header...");
        Object jwkObject = jwsObject.getHeader().toJSONObject().get("jwk");
        if (jwkObject == null) {
            throw new BadJOSEException("Missing 'jwk' in JWT header.");
        }
        String jwkString = new ObjectMapper().writeValueAsString(jwkObject);
        logger.info("Extracted JWK: {}", jwkString);
        JWK jwk = JWK.parse(jwkString);
        if (!(jwk instanceof ECKey)) {
            throw new BadJOSEException("Invalid key type, expected ECKey.");
        }
        return jwk;
    }

    private String extractAccountCert(SignedJWT signedJWT) {
        logger.info("Extracting accountCert from transaction JWT header...");
        // Now extracting accountCert from the header of the transactionJWT, not mainJWT
        Object accountCertObj = signedJWT.getHeader().toJSONObject().get("accountJwt");
        if (accountCertObj == null) {
            throw new IllegalArgumentException("Missing accountJwt in transaction JWT header.");
        }
        String accountCert = accountCertObj.toString();
        logger.info("Extracted accountCert: {}", accountCert);
        return accountCert;
    }

    private ECKey loadPublicKey() throws ParseException {
        logger.info("Loading public key from configured backend...");
        if (serverPublicKeyJson == null || serverPublicKeyJson.isEmpty()) {
            throw new IllegalStateException("Public key JSON is not configured properly.");
        }
        logger.info("server public key: {}", serverPublicKeyJson);
        JWK jwk = JWK.parse(serverPublicKeyJson);
        logger.info("Loaded JWK from backend: {}", jwk);

        if (!(jwk instanceof ECKey publicKey) || jwk.isPrivate()) {
            throw new IllegalArgumentException("Invalid JWK provided by backend.");
        }
        logger.info("Successfully loaded public ECKey: {}", publicKey);
        return publicKey;
    }

    private static boolean verifySignature(JWSObject jwsObject, ECKey ecKey)
            throws JOSEException, BadJWTException {
        logger.info("Verifying signature for JWS object...");
        var verifier = ecKey.toECPublicKey();
        return jwsObject.verify(new ECDSAVerifier(verifier));
    }
}
