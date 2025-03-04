/**package com.adorsys.webank.obs.serviceimpl;



import static org.junit.jupiter.api.Assertions.*;

import com.nimbusds.jose.jwk.*;
import com.adorsys.webank.obs.security.JwtCertValidator;
import com.nimbusds.jwt.*;
import org.junit.jupiter.api.Test;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.gen.*;
import java.lang.reflect.Field;


class JwtCertValidatorTest {

    // Test Case 1: Valid JWT with correct signatures
    @Test
     void testValidJwt_ReturnsTrue() throws  Exception {
        // Generate EC key pair for phoneNumberCert
        ECKey phoneNumberKey = generateECKey();
        String publicKeyJson = phoneNumberKey.toPublicJWK().toJSONString();

        // Create valid phoneNumberCert JWT
        SignedJWT phoneNumberJwt = createSignedJWT(phoneNumberKey, "sub", "user123");
        String outerToken = createOuterToken(phoneNumberJwt.serialize());

        JwtCertValidator validator = createValidator(publicKeyJson);
        assertTrue(validator.validateJWT(outerToken));
    }

    // Test Case 2: Invalid outer JWT format
    @Test
     void testInvalidJwtFormat_ReturnsFalse() throws Exception {
        JwtCertValidator validator = createValidator("dummy-key");
        assertFalse(validator.validateJWT("invalid.token"));
    }

    // Test Case 3: Missing phoneNumberJwt in header
    @Test
     void testMissingPhoneNumberJwt_ReturnsFalse() throws Exception {
        // Create outer JWT without phoneNumberJwt header
        SignedJWT outerJwt = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.ES256).build(),
                new JWTClaimsSet.Builder().build()
        );
        outerJwt.sign(new ECDSASigner(generateECKey()));

        JwtCertValidator validator = createValidator("dummy-key");
        assertFalse(validator.validateJWT(outerJwt.serialize()));
    }

    // Test Case 4: Empty public key configuration
    @Test
     void testEmptyPublicKey_ReturnsFalse() throws Exception {
        SignedJWT phoneNumberJwt = createSignedJWT(generateECKey(), "sub", "user123");
        JwtCertValidator validator = createValidator("");
        assertFalse(validator.validateJWT(createOuterToken(phoneNumberJwt.serialize())));
    }

    // Test Case 5: Non-EC public key
    @Test
     void testNonEcPublicKey_ReturnsFalse() throws Exception {
        RSAKey rsaKey = new RSAKeyGenerator(2048).generate();
        JwtCertValidator validator = createValidator(rsaKey.toPublicJWK().toJSONString());
        assertFalse(validator.validateJWT(createOuterToken("any-cert")));
    }

    // Test Case 6: EC private key instead of public
    @Test
     void testEcPrivateKey_ReturnsFalse() throws Exception {
        ECKey privateKey = generateECKey();
        JwtCertValidator validator = createValidator(privateKey.toJSONString());
        assertFalse(validator.validateJWT(createOuterToken("any-cert")));
    }

    // Test Case 7: Signature verification failure
    @Test
     void testSignatureVerificationFailure_ReturnsFalse() throws Exception {
        ECKey validKey = generateECKey();
        ECKey invalidKey = generateECKey();

        SignedJWT phoneNumberJwt = createSignedJWT(validKey, "sub", "user123");
        JwtCertValidator validator = createValidator(invalidKey.toPublicJWK().toJSONString());

        assertFalse(validator.validateJWT(createOuterToken(phoneNumberJwt.serialize())));
    }

    // Test Case 8: Invalid public key JSON
    @Test
     void testInvalidPublicKeyJson_ReturnsFalse() throws Exception {
        JwtCertValidator validator = createValidator("{invalid-json}");
        assertFalse(validator.validateJWT(createOuterToken("any-cert")));
    }

    // Helper methods
    private ECKey generateECKey() throws JOSEException {
        return new ECKeyGenerator(Curve.P_256).generate();
    }

    private SignedJWT createSignedJWT(ECKey key, String claimKey, String claimValue) throws JOSEException {
        JWTClaimsSet claims = new JWTClaimsSet.Builder().claim(claimKey, claimValue).build();
        SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.ES256).build(), claims);
        jwt.sign(new ECDSASigner(key));
        return jwt;
    }

    private String createOuterToken(String phoneNumberCert) throws JOSEException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                .customParam("phoneNumberJwt", phoneNumberCert)
                .build();
        SignedJWT outerJwt = new SignedJWT(header, new JWTClaimsSet.Builder().build());
        outerJwt.sign(new ECDSASigner(generateECKey()));
        return outerJwt.serialize();
    }

    private JwtCertValidator createValidator(String publicKeyJson) throws NoSuchFieldException, IllegalAccessException {
        JwtCertValidator validator = new JwtCertValidator();
        setPrivateField(validator, publicKeyJson);
        return validator;
    }

    private void setPrivateField(Object target, Object value) throws NoSuchFieldException, IllegalAccessException  {
        Field field = target.getClass().getDeclaredField("SERVER_PUBLIC_KEY_JSON");
        field.setAccessible(true);
        field.set(target, value);
    }
}
*/