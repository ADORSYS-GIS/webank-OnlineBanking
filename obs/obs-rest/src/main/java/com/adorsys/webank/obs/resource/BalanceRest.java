package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.dto.response.BalanceResponse;
import com.adorsys.webank.obs.service.*;

import java.time.LocalDateTime;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BalanceRest implements BalanceRestApi {

    private final BalanceServiceApi balanceService;

    /**
     * Handles balance requests for certified accounts.
     * Requires the user to have the ROLE_ACCOUNT_CERTIFIED and be authenticated.
     *
     * @param authorizationHeader The authorization header containing the user's credentials.
     * @param balanceRequest      The request body containing account details for balance retrieval.
     * @return ResponseEntity with the balance information or an error message.
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ACCOUNT_CERTIFIED') and isAuthenticated()")
    public ResponseEntity<BalanceResponse> getBalance(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                             @RequestBody BalanceRequest balanceRequest) {
        log.info("Incoming balance request: {}", balanceRequest);

        try {
            BalanceResponse response = balanceService.getBalance(balanceRequest);
            log.info("Balance request processed successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing balance request", e);
            BalanceResponse errorResponse = new BalanceResponse();
            errorResponse.setAccountId(balanceRequest.getAccountID());
            errorResponse.setStatus(BalanceResponse.BalanceStatus.SYSTEM_ERROR);
            errorResponse.setMessage("An error occurred while processing the request");
            errorResponse.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}

