package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.security.JwtCertValidator;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtCertValidatorTest {

    private JwtCertValidator validator;
    private ECKey ecJwk;

    @BeforeEach
    void setUp() throws JOSEException {
        ecJwk = new ECKeyGenerator(Curve.P_256)
                .keyID("test-key-id")
                .generate();
        String publicKeyJson = ecJwk.toPublicJWK().toJSONString();

        // Instantiate the validator and inject the public key JSON
        validator = new JwtCertValidator();
        ReflectionTestUtils.setField(validator, "serverPublicKeyJson", publicKeyJson);
    }

    /**
     * Helper method to generate a signed certificate JWT.
     * This JWT will be used as the certificate embedded in the outer JWT header.
     */
    String generateCertJWT(ECKey signingKey) throws JOSEException {
        JWSSigner signer = new ECDSASigner(signingKey);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("test-issuer")
                .issueTime(new Date())
                .build();

        SignedJWT certJwt = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(signingKey.getKeyID()).build(),
                claimsSet
        );
        certJwt.sign(signer);
        return certJwt.serialize();
    }

    /**
     * Helper method to generate an outer JWT with the provided header entry for cert.
     */
    private String generateOuterJWT(String certJwt) throws JOSEException {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("outer-issuer")
                .issueTime(new Date())
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                .customParam("accountJwt", certJwt)
                .build();

        SignedJWT outerJwt = new SignedJWT(header, claimsSet);
        outerJwt.sign(new ECDSASigner(ecJwk));
        return outerJwt.serialize();
    }

    @Test
    void testValidateJWT_Success() throws JOSEException {
        String certJwt = generateCertJWT(ecJwk);
        String outerJwt = generateOuterJWT(certJwt);

        boolean isValid = validator.validateJWT(outerJwt);
        assertTrue(isValid, "Expected JWT to be valid");
    }

    @Test
    void testValidateJWT_InvalidSignature() throws JOSEException {
        String certJwt = generateCertJWT(ecJwk);
        String tamperedCertJwt = certJwt.substring(0, certJwt.length() - 2) + "aa";

        String outerJwt = generateOuterJWT(tamperedCertJwt);

        boolean isValid = validator.validateJWT(outerJwt);
        assertFalse(isValid, "Expected JWT validation to fail due to invalid signature");
    }

    @Test
    void testValidateJWT_MissingCertificateField() throws JOSEException {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("outer-issuer")
                .issueTime(new Date())
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).build();
        SignedJWT outerJwt = new SignedJWT(header, claimsSet);
        outerJwt.sign(new ECDSASigner(ecJwk));
        String token = outerJwt.serialize();

        boolean isValid = validator.validateJWT(token);
        assertFalse(isValid, "Expected JWT validation to fail due to missing certificate field");
    }

    @Test
    void testValidateJWT_InvalidPublicKeyConfiguration() throws JOSEException {
        ReflectionTestUtils.setField(validator, "serverPublicKeyJson", "");

        String certJwt = generateCertJWT(ecJwk);
        String outerJwt = generateOuterJWT(certJwt);

        boolean isValid = validator.validateJWT(outerJwt);
        assertFalse(isValid, "Expected JWT validation to fail due to invalid public key configuration");
    }
}
