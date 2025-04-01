package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.RecoveryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Recovery", description = "Operations related to account recovery")
@RequestMapping("/api/accounts")
public interface RecoveryRestApi {

    @Operation(summary = "Recover Account", description = "Initiate the account recovery process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recovery process initiated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request for account recovery"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/recovery", consumes = "application/json", produces = "application/json")
    ResponseEntity<String> recoverAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody RecoveryDto recoveryDto);
}
