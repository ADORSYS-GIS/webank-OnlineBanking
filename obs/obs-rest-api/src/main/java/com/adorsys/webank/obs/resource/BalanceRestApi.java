package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.dto.BalanceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Balance", description = "Operations related to Balance processing")
@RequestMapping("/api/accounts")
public interface BalanceRestApi {

    @Operation(summary = "Get Balance", description = "Get the balance for a particular account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request to get Balance"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable")

    })
    @PostMapping(value = "/balance", consumes = "application/json", produces = "application/json")
    ResponseEntity<BalanceResponse> getBalance(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody BalanceRequest request);

}
