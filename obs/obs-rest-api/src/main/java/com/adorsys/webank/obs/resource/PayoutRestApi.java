package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.PayoutRequest;
import com.adorsys.webank.obs.dto.TransRequest;
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

@Tag(name = "Trans", description = "Operations related to Trans processing")
@RequestMapping("/api/accounts/")
public interface PayoutRestApi {


    @Operation(summary = "Topup a an Account", description = "Topup an account from another Account (Payout)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account Topup successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request to Topup account"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    @PostMapping(value = "/payout", consumes = "application/json", produces = "application/json")
    ResponseEntity<String> payout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody PayoutRequest request);

}
