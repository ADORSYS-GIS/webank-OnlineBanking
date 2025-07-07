package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.dto.response.MoneyTransferResponse;
import com.adorsys.webank.obs.service.WithdrawServiceApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import java.time.LocalDateTime;

/**
 * REST controller for handling withdrawal operations.
 * Provides endpoints for withdrawing funds from user accounts.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class WithdrawRestServer implements WithdrawRestApi {

    private final WithdrawServiceApi withdrawServiceApi;

    /**
     * Processes a withdrawal request from a certified account.
     * Requires the user to have ROLE_ACCOUNT_CERTIFIED and be authenticated.
     *
     * @param authorizationHeader The authorization header containing the JWT token
     * @param request The withdrawal request containing account and amount details
     * @return ResponseEntity containing the result of the withdrawal operation:
     *         - On success: Returns MoneyTransferResponse with COMPLETED status
     *         - On failure: Returns MoneyTransferResponse with appropriate error status
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ACCOUNT_CERTIFIED') and isAuthenticated()")
    public ResponseEntity<MoneyTransferResponse> withdraw(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody MoneyTransferRequestDto request) {

        try {
            log.info("Incoming withdrawal request for account: {}", request.getSenderAccountId());
            MoneyTransferResponse result = withdrawServiceApi.withdraw(request);

            // Log and return based on status
            return switch (result.getStatus()) {
                case COMPLETED -> {
                    log.info("Withdrawal completed for account: {}", request.getSenderAccountId());
                    yield ResponseEntity.ok(result);
                }
                case INSUFFICIENT_FUNDS -> {
                    log.warn("Withdrawal failed due to insufficient funds for account: {}", request.getSenderAccountId());
                    yield ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                }
                case INVALID_ACCOUNT -> {
                    log.warn("Withdrawal failed due to invalid account for account: {}", request.getSenderAccountId());
                    yield ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
                }
                case SYSTEM_ERROR -> {
                    log.error("Withdrawal failed due to system error for account: {}", request.getSenderAccountId());
                    yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                }
                case PENDING -> {
                    log.info("Withdrawal is pending for account: {}", request.getSenderAccountId());
                    yield ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
                }
                default -> {
                    log.error("Unexpected status for withdrawal request: {}", result.getStatus());
                    yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                }
            };

        } catch (Exception e) {
            log.error("Exception occurred during withdrawal for account: {}", request.getSenderAccountId(), e);
            MoneyTransferResponse errorResponse = new MoneyTransferResponse();
            errorResponse.setStatus(MoneyTransferResponse.TransferStatus.SYSTEM_ERROR);
            errorResponse.setMessage("An unexpected error occurred: " + e.getMessage());
            errorResponse.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
