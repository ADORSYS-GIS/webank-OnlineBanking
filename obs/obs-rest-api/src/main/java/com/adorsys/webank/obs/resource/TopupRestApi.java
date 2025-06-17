package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TopupRequestDto;
import com.adorsys.webank.obs.dto.response.ErrorResponse;
import com.adorsys.webank.obs.dto.response.TopupResponse;
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
    name = "Account Top-up",
    description = "APIs for topping up account balances"
)
@RequestMapping("/api/accounts")
public interface TopupRestApi {

    @Operation(
        summary = "Top up account balance",
        description = """
            Adds funds to an account balance.
            The account must be active and the top-up amount must be within allowed limits.
            The operation can be performed by the account owner or authorized agents.
            """,
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Top-up completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TopupResponse.class)
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
            description = "Forbidden - User does not have permission to top up",
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
        value = "/agent/topup",
        consumes = "application/json",
        produces = "application/json"
    )
    ResponseEntity<TopupResponse> topup(
        @Parameter(
            description = "JWT token in the format 'Bearer <token>'",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
        
        @Parameter(
            description = "Top-up request containing account and amount details",
            required = true
        )
        @RequestBody TopupRequestDto request
    );
} 