package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.JwtValidator;
import com.adorsys.webank.obs.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransRest implements TransRestApi {

    private static final Logger log = LoggerFactory.getLogger(TransRest.class);
    private TransServiceApi transService;
    public TransRest( TransServiceApi transService) {
        this.transService = transService;
    }

    @Override
    public ResponseEntity<String> getTrans(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody TransRequest request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body cannot be null.");
        }
        try {
            String jwtToken = extractJwtFromHeader(authorizationHeader);
            JwtValidator.validateAndExtract(jwtToken, request.getAccountID());
            log.info("Transaction request validated successfully");
            String result = transService.getTrans(request, jwtToken) ;
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
