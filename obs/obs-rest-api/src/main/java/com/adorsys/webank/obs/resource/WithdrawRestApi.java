package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.WithdrawRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Trans", description = "Operations related to transaction processing")
@RequestMapping("/api/accounts/")
public interface WithdrawRestApi {

    @Operation(summary = "Withdraw from an account", description = "Withdraw funds from an account to another account (Payout)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account withdrawal successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request to withdraw funds"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/withdraw", consumes = "application/json", produces = "application/json")
    ResponseEntity<String> withdraw(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody WithdrawRequest request);
}
