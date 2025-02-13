package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.RegistrationRequest;
import com.adorsys.webank.obs.service.RegistrationServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationResource implements RegistrationResourceApi {

    @Autowired
    private RegistrationServiceApi registrationService;
    @Override
    @PostMapping
    public ResponseEntity<String> registerAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody RegistrationRequest registrationRequest) {
        if (registrationRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body cannot be null.");
        }
        try {
            String jwtToken = extractJwtFromHeader(authorizationHeader);
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
