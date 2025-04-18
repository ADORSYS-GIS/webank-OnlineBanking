package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.security.JwtValidator;
import com.adorsys.webank.obs.service.WithdrawServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WithdrawRestServer implements WithdrawRestApi {

    private static final Logger log = LoggerFactory.getLogger(WithdrawRestServer.class);
    private final WithdrawServiceApi withdrawServiceApi;

    public WithdrawRestServer(WithdrawServiceApi withdrawServiceApi) {
        this.withdrawServiceApi = withdrawServiceApi;
    }

    @Override
    public ResponseEntity<String> withdraw(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody MoneyTransferRequestDto request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body cannot be null.");
        }
        try {
            String jwtToken = extractJwtFromHeader(authorizationHeader);
            JwtValidator.validateAndExtract(jwtToken, request.getSenderAccountId(), request.getAmount(), request.getRecipientAccountId());
            log.info("Withdrawal request validated successfully");
            String result = withdrawServiceApi.withdraw(request, jwtToken) ;
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
