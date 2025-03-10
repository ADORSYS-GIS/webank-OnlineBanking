package com.adorsys.webank.obs.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
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

            // Extract and validate the accountId from both `accountJwt` and `transactionJWT`
            if (!compareAccountIds(accountCertJwt, transactionJWT)) {
                logger.warn("Account ID mismatch between accountJwt and transactionJWT.");
                return false;
            }
            logger.info("Account ID comparison verified successfully.");

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
            throws JOSEException {
        logger.info("Verifying signature for JWS object...");
        var verifier = ecKey.toECPublicKey();
        return jwsObject.verify(new ECDSAVerifier(verifier));
    }

    /**
     * Compares the `accountId` between the `accountJwt` and `transactionJwt`.
     *
     * @param accountCertJwt The accountJwt parsed as a SignedJWT.
     * @param transactionJWT The transaction JWT parsed as a SignedJWT.
     * @return True if the accountIds match, false otherwise.
     */
    private boolean compareAccountIds(SignedJWT accountCertJwt, SignedJWT transactionJWT) {
        try {
            // Extract the payloads
            JWTClaimsSet accountCertClaims = accountCertJwt.getJWTClaimsSet();
            JWTClaimsSet transactionClaims = transactionJWT.getJWTClaimsSet();

            // Extract accountId from both JWTs
            String accountIdFromAccountCert = accountCertClaims.getStringClaim("acc");
            String accountIdFromTransaction = transactionClaims.getStringClaim("accountId");

            logger.debug("Extracted accountId from accountCert: {}", accountIdFromAccountCert);
            logger.debug("Extracted accountId from transactionJWT: {}", accountIdFromTransaction);


            // Compare the accountId from both JWTs
            logger.info("Comparing accountId: {} with accountId: {}", accountIdFromAccountCert, accountIdFromTransaction);

            return accountIdFromAccountCert != null && accountIdFromAccountCert.equals(accountIdFromTransaction);
        } catch (Exception e) {
            logger.error("Error comparing accountIds: ", e);
            return false;
        }
    }
}
