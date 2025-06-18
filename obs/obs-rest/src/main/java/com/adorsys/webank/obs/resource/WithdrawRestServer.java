package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
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

@RestController
@Slf4j
@RequiredArgsConstructor
public class WithdrawRestServer implements WithdrawRestApi {

    private final WithdrawServiceApi withdrawServiceApi;

    /**
     * Handles withdrawal requests from certified accounts.
     * Requires the user to have the ROLE_ACCOUNT_CERTIFIED and be authenticated.
     *
     * @param authorizationHeader The authorization header containing the user's credentials.
     * @param request             The request body containing withdrawal details.
     * @return ResponseEntity with the result of the withdrawal process or an error message.
     */

    @Override
    @PreAuthorize("hasRole('ROLE_ACCOUNT_CERTIFIED') and isAuthenticated()")
    public ResponseEntity<String> withdraw(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody MoneyTransferRequestDto request) {

        try {
            String result = withdrawServiceApi.withdraw(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            log.error("Error processing withdrawal request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request: " + e.getMessage());
        }
    }
}
