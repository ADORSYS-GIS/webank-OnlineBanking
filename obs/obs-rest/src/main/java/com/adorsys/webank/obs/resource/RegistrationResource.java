package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.service.RegistrationServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> registerAccount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        log.info("Processing registration with Authorization header: {}", authorizationHeader);

        try {
            String result = registrationService.registerAccount(authorizationHeader);
            log.info("Registration successful");
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Registration failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the registration.");
        }
    }
}
