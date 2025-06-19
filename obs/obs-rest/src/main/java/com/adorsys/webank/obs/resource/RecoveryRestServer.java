package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.RecoveryDto;
import com.adorsys.webank.obs.service.RecoveryServiceApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RecoveryRestServer implements RecoveryRestApi {

    private final RecoveryServiceApi recoveryServiceApi;

    /**
     * Handles account recovery requests.
     * Requires the user to be authenticated.
     *
     * @param authorizationHeader The authorization header containing the user's credentials.
     * @param recoveryDto         The request body containing account details for recovery.
     * @return ResponseEntity with the result of the recovery process or an error message.
     */

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> recoverAccount(String authorizationHeader, RecoveryDto recoveryDto) {
        try {
            String accountId = recoveryDto.getAccountId();
            log.info("Recovery request received for accountId: {}", accountId);

            String accountCert = recoveryServiceApi.recoverAccount(accountId);

            if (accountCert != null) {
                log.info("Account recovery successful for accountId: {}", accountId);
                return ResponseEntity.ok(accountCert);
            } else {
                log.warn("Account recovery failed for accountId: {}", accountId);
                return ResponseEntity.badRequest().body("Failed to initiate recovery");
            }

        } catch (Exception e) {
            log.error("Account recovery failed due to an internal error", e);
            return ResponseEntity.badRequest().body("Invalid authentication token");
        }
    }
}