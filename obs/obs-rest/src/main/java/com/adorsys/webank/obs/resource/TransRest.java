package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TransRest implements TransRestApi {


    private  final TransServiceApi transService;

    /**
     * Handles transaction requests.
     *
     * @param authorizationHeader The Authorization header containing the JWT token.
     * @param request The transaction request body.
     * @return ResponseEntity with the result of the transaction processing.
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ACCOUNT_CERTIFIED') and isAuthenticated()")
    public ResponseEntity<String> getTrans(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody TransRequest request) {

        try {
            log.info("Received transaction request for: {}", request);
            String result = transService.getTrans(request);
            log.info("Transaction processed successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Error occurred while processing transaction request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the transaction request.");
        }
    }
}
