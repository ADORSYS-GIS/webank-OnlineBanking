package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TopupRequestDto;
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

@Tag(name = "Topup", description = "Operations related to account topup")
@RequestMapping("/api/accounts/")
public interface TopupRestApi {
    @Operation(summary = "Topup an Account", description = "Add funds to an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account topup successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request to topup account"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/agent/topup", consumes = "application/json", produces = "application/json")
    ResponseEntity<String> topup(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody TopupRequestDto request);
} 