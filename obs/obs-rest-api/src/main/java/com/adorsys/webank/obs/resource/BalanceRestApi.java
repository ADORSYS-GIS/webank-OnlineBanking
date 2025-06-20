package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.dto.response.BalanceResponse;
import com.adorsys.webank.obs.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Account Balance",
    description = "APIs for retrieving and managing account balances"
)
@RequestMapping("/api/accounts")
public interface BalanceRestApi {

    @Operation(
        summary = "Get account balance",
        description = """
            Retrieves the current balance for a specified account.
            The account must be active and the user must have permission to access it.
            The balance is returned in the account's default currency.
            """,
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Balance retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BalanceResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request - Missing or invalid request parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid or missing JWT token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - User does not have access to the account",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PostMapping(
        value = "/balance",
        consumes = "application/json",
        produces = "application/json"
    )
    ResponseEntity<BalanceResponse> getBalance(
        @Parameter(
            description = "JWT token in the format 'Bearer <token>'",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
        
        @Parameter(
            description = "Balance request containing account details",
            required = true
        )
        @RequestBody BalanceRequest request
    );
}
