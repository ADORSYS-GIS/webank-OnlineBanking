package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.dto.response.ErrorResponse;
import com.adorsys.webank.obs.dto.response.MoneyTransferResponse;
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
    name = "Account Withdrawal",
    description = "APIs for withdrawing funds from accounts"
)
@RequestMapping("/api/accounts")
public interface WithdrawRestApi {

    @Operation(
        summary = "Withdraw funds from account",
        description = """
            Withdraws funds from an account.
            The account must have sufficient funds and the user must have permission to withdraw.
            The withdrawal amount must be within the allowed limits for the account.
            """,
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Withdrawal completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MoneyTransferResponse.class)
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
            description = "Forbidden - User does not have permission to withdraw",
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
        value = "/withdraw",
        consumes = "application/json",
        produces = "application/json"
    )
    ResponseEntity<MoneyTransferResponse> withdraw(
        
        @Parameter(
            description = "Withdrawal request containing account and amount details",
            required = true
        )
        @RequestBody MoneyTransferRequestDto request
    );
}
