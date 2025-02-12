package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class BalanceRest implements BalanceRestApi {

    private  BalanceServiceApi balanceService;

    public BalanceRest( BalanceServiceApi balanceService) {
        this.balanceService = balanceService;
    }

    @Override
    public ResponseEntity<String> getBalance(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody BalanceRequest balanceRequest) {
        // Check for null balanceRequest
        if (balanceRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body cannot be null.");
        }

        try {
            String jwtToken = extractJwtFromHeader(authorizationHeader);
            String result = balanceService.getBalance(balanceRequest, jwtToken) ;
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

