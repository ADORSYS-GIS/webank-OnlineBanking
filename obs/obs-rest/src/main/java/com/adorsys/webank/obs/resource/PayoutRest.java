package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.dto.response.MoneyTransferResponse;
import com.adorsys.webank.obs.service.PayoutServiceApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/transfers")
public class PayoutRest implements PayoutRestApi {

    private final PayoutServiceApi payoutService;

    /**
     * Handles payout requests for certified accounts.
     * Requires the user to have the ROLE_ACCOUNT_CERTIFIED or ROLE_KYC_CERT and be authenticated.
     *
     * @param authorizationHeader The authorization header containing the user's credentials.
     * @param request             The request body containing payout details.
     * @return ResponseEntity with the result of the payout processing or an error message.
     */

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ACCOUNT_CERTIFIED', 'ROLE_KYC_CERT') and isAuthenticated()")
    public ResponseEntity<MoneyTransferResponse> payout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody MoneyTransferRequestDto request) {

        try {
            log.info("Received payout request for sender account ID: {}", request.getSenderAccountId());
            MoneyTransferResponse response = payoutService.payout(request);
            log.info("Payout processed successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred during payout processing", e);
            MoneyTransferResponse errorResponse = new MoneyTransferResponse();
            errorResponse.setStatus(MoneyTransferResponse.TransferStatus.SYSTEM_ERROR);
            errorResponse.setMessage("An error occurred while processing the payout request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
