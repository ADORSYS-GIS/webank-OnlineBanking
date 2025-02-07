package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.service.TransServiceApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class TransRest implements TransRestApi {
    @Autowired
    private TransServiceApi transService;

    @Override
    public ResponseEntity<String> getTrans(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody TransRequest request) {
        try {
            String jwtToken = extractJwtFromHeader(authorizationHeader);
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
