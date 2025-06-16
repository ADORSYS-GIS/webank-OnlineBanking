package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.dto.response.ErrorResponse;
import com.adorsys.webank.obs.dto.response.TransactionHistoryResponse;
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
    name = "Transaction History",
    description = "APIs for retrieving account transaction history"
)
@RequestMapping("/api/accounts")
public interface TransRestApi {

    @Operation(
        summary = "Get account transaction history",
        description = """
            Retrieves the transaction history for a specified account.
            The user must have permission to view the account's transactions.
            Results can be filtered by date range and transaction type.
            """,
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Transaction history retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TransactionHistoryResponse.class)
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
            description = "Forbidden - User does not have permission to view transactions",
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
    @GetMapping(
        value = "/transactions",
        produces = "application/json"
    )
    ResponseEntity<TransactionHistoryResponse> getTransactionHistory(
        @Parameter(
            description = "JWT token in the format 'Bearer <token>'",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
        
        @Parameter(
            description = "Transaction history request containing account and filter details",
            required = true
        )
        @RequestBody TransRequest request
    );
}
