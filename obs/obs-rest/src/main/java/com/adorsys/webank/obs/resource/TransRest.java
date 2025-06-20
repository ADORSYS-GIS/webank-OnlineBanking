package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.dto.response.TransactionHistoryResponse;
import com.adorsys.webank.obs.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TransRest implements TransRestApi {

    private final TransServiceApi transService;

    /**
     * Handles transaction requests.
     *
     * @param authorizationHeader The Authorization header containing the JWT token.
     * @param request The transaction request body.
     * @return ResponseEntity with the result of the transaction processing.
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ACCOUNT_CERTIFIED') and isAuthenticated()")
    public ResponseEntity<TransactionHistoryResponse> getTrans(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody TransRequest request) {

        try {
            log.info("Received transaction request for account: {}", request.getAccountID());
            TransactionHistoryResponse result = transService.getTrans(request);
            
            if (result.getStatus() == TransactionHistoryResponse.TransactionStatus.SUCCESS) {
                log.info("Transaction history retrieved successfully for account: {}", request.getAccountID());
                return ResponseEntity.ok(result);
            } else {
                log.error("Failed to retrieve transaction history: {}", result.getMessage());
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            log.error("Error occurred while processing transaction request", e);
            TransactionHistoryResponse errorResponse = new TransactionHistoryResponse();
            errorResponse.setStatus(TransactionHistoryResponse.TransactionStatus.FAILED);
            errorResponse.setMessage("Failed to retrieve transaction history: " + e.getMessage());
            errorResponse.setTimestamp(LocalDateTime.now());
            errorResponse.setData("[]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
