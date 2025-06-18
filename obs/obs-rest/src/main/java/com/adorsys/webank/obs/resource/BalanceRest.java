package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.service.*;
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
    public ResponseEntity<String> getBalance(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                             @RequestBody BalanceRequest balanceRequest) {
        log.info("Incoming balance request: {}", balanceRequest);

        try {
            String result = balanceService.getBalance(balanceRequest);
            log.info("Balance request processed successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Error processing balance request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request.");
        }
    }


}

