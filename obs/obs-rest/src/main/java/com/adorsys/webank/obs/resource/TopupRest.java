package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TopupRequestDto;
import com.adorsys.webank.obs.dto.response.TopupResponse;
import com.adorsys.webank.obs.service.TopupServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for handling account top-up operations.
 * This controller provides endpoints for adding funds to user accounts.
 * All operations require proper authentication and account certification.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TopupRest implements TopupRestApi {

    private final TopupServiceApi topupService;

    /**
     * Processes a top-up request for a certified account.
     * This endpoint requires the user to have ROLE_ACCOUNT_CERTIFIED and be authenticated.
     * The operation will add the specified amount to the user's account.
     *
     * @param request The top-up request containing account ID and amount details
     * @return ResponseEntity containing the result of the top-up operation:
     *         - On success: Returns TopupResponse with COMPLETED status
     *         - On failure: Returns TopupResponse with FAILED status and error message
     * @throws Exception if the top-up operation fails due to system errors
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ACCOUNT_CERTIFIED') and isAuthenticated()")
    public ResponseEntity<TopupResponse> topup(TopupRequestDto request) {
        try {
            log.info("Top-up request received for accountId: {}", request.getAccountId());

            TopupResponse result = topupService.topup(request);

            log.info("Top-up processed successfully for accountId: {}", request.getAccountId());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Top-up processing failed for accountId: {} - {}", request.getAccountId(), e.getMessage(), e);
            TopupResponse errorResponse = new TopupResponse();
            errorResponse.setStatus(TopupResponse.TopupStatus.FAILED);
            errorResponse.setMessage("An error occurred while processing the request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}