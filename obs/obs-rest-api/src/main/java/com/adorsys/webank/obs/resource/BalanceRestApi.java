package com.adorsys.webank.obs.resource;


import com.adorsys.webank.obs.dto.BalanceRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Balance", description = "Operations related to Balance processing")
@RequestMapping("/obs/accounts/")
public interface BalanceRestApi {

    @Operation(summary = "Get Balance", description = "Get the balance for a particular account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request to get Balance")
    })
    @PostMapping(value = "/balance", consumes = "application/json", produces = "application/json")
    String getBalance(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody BalanceRequest request);

}
