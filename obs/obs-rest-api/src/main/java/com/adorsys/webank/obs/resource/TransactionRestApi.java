package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TransactionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transaction", description = "Operations related to Transaction processing")
@RequestMapping("/obs/accounts/")
public interface TransactionRestApi {


    @Operation(summary = "Get Transaction", description = "Get the Transaction for a particular account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request to get Transaction")
    })
    @PostMapping(value = "/transaction", consumes = "application/json", produces = "application/json")
    String getTransaction(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody TransactionRequest request);

}
