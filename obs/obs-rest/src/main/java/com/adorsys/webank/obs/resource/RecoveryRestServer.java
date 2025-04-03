package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.RecoveryDto;
import com.adorsys.webank.obs.security.JwtValidator;
import com.adorsys.webank.obs.security.JwtCertValidator;
import com.adorsys.webank.obs.service.RecoveryServiceApi;
import com.nimbusds.jose.jwk.JWK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecoveryRestServer implements RecoveryRestApi {

    private static final Logger log = LoggerFactory.getLogger(RecoveryRestServer.class);
    private final RecoveryServiceApi recoveryServiceApi;
    private final JwtCertValidator jwtCertValidator;

    public RecoveryRestServer(RecoveryServiceApi recoveryServiceApi, JwtCertValidator jwtCertValidator) {
        this.recoveryServiceApi = recoveryServiceApi;
        this.jwtCertValidator = jwtCertValidator;
    }

    @Override
    public ResponseEntity<String> recoverAccount(String authorizationHeader, RecoveryDto recoveryDto) {
        String jwtToken;
        JWK publicKey;
        try {
            // Extract and validate the JWT token from the Authorization header
            jwtToken = extractJwtFromHeader(authorizationHeader);
            publicKey = JwtValidator.validateAndExtract(jwtToken);
            log.info("JWT validation successful");
            jwtCertValidator.validateJWT(jwtToken);

        } catch (Exception e) {
            log.error("JWT validation failed", e);
            return ResponseEntity.badRequest().body("Invalid authentication token");
        }

        String accountId = recoveryDto.getAccountId();

        String accountCert = recoveryServiceApi.recoverAccount(accountId, publicKey.toJSONString());

        // Delegate recovery request to the service layer
        if (accountCert != null) {
            return ResponseEntity.ok(accountCert);
        } else {
            return ResponseEntity.badRequest().body("Failed to initiate recovery");
        }
    }

    private String extractJwtFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header must start with 'Bearer '");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
}
