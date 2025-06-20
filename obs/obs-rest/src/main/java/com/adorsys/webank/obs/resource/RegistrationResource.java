package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.response.RegistrationResponse;
import com.adorsys.webank.obs.service.RegistrationServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling account registration operations.
 * Provides endpoints for registering new accounts in the system.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class RegistrationResource implements RegistrationResourceApi {

    private final RegistrationServiceApi registrationService;

    /**
     * Handles account registration requests.
     *
     * @param authorizationHeader The Authorization header containing the JWT token.
     * @return ResponseEntity with the result of the registration process.
     */
    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RegistrationResponse> registerAccount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        log.info("Processing registration request");

        try {
            RegistrationResponse response = registrationService.registerAccount(authorizationHeader);
            log.info("Registration successful for account: {}", response.getAccountId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Registration failed", e);
            RegistrationResponse errorResponse = new RegistrationResponse();
            errorResponse.setStatus(RegistrationResponse.RegistrationStatus.FAILED);
            errorResponse.setMessage("An error occurred while processing the registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
