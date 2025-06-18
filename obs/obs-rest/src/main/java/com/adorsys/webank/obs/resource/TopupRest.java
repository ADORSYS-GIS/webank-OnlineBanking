package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TopupRequestDto;
import com.adorsys.webank.obs.service.TopupServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TopupRest implements TopupRestApi {

    private final TopupServiceApi topupService;

    /**
     * Handles top-up requests for certified accounts.
     * Requires the user to have the ROLE_ACCOUNT_CERTIFIED and be authenticated.
     *
     * @param authorizationHeader The authorization header containing the user's credentials.
     * @param request             The request body containing top-up details.
     * @return ResponseEntity with the result of the top-up processing or an error message.
     */

    @Override
    @PreAuthorize("hasRole('ROLE_ACCOUNT_CERTIFIED') and isAuthenticated()")
    public ResponseEntity<String> topup(String authorizationHeader, TopupRequestDto request) {

        try {
            log.info("Top-up request received for accountId: {}", request.getAccountId());

            String result = topupService.topup(request);

            log.info("Top-up processed successfully for accountId: {}", request.getAccountId());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            log.error("Top-up processing failed for accountId: {} - {}", request.getAccountId(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request: " + e.getMessage());
        }
    }
}