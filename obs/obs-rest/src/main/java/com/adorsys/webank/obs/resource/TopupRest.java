package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TopupRequestDto;
import com.adorsys.webank.obs.security.JwtValidator;
import com.adorsys.webank.obs.service.TopupServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TopupRest implements TopupRestApi {

    private final TopupServiceApi topupService;

    @Override
    public ResponseEntity<String> topup(String authorizationHeader, TopupRequestDto request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body cannot be null.");
        }
        try {
            String jwtToken = extractJwtFromHeader(authorizationHeader);
            JwtValidator.validateAndExtract(jwtToken, request.getAccountId(), request.getAmount());
            log.info("Topup request validated successfully");
            String result = topupService.topup(request, jwtToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Error processing topup request: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request: " + e.getMessage());
        }
    }

    private String extractJwtFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header must start with 'Bearer '");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
} 