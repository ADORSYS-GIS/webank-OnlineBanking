package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.JwtValidator;
import com.adorsys.webank.obs.service.*;
import com.nimbusds.jose.jwk.JWK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationResource implements RegistrationResourceApi {


    private static final Logger log = LoggerFactory.getLogger(RegistrationResource.class);
    private RegistrationServiceApi registrationService;

   public RegistrationResource( RegistrationServiceApi registrationService) {
       this.registrationService = registrationService;
   }

    @Override
    @PostMapping
    public ResponseEntity<String> registerAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody RegistrationRequest registrationRequest) {
        if (registrationRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body cannot be null.");
        }
        try {
            String jwtToken = extractJwtFromHeader(authorizationHeader);
            JwtValidator.validateAndExtract(jwtToken, registrationRequest.getPhoneNumber(), registrationRequest.getPublicKey());
            log.info("Registration request validated successfully");
            String result = registrationService.registerAccount(registrationRequest, jwtToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            // Log the exception (optional)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    private String extractJwtFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header must start with 'Bearer '");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
}
