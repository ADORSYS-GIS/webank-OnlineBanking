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
    name = "Money Transfer",
    description = "APIs for transferring money between accounts"
)
@RequestMapping("/api/transfers")
public interface PayoutRestApi {

    @Operation(
        summary = "Transfer money between accounts",
        description = """
            Transfers money from one account to another.
            The sender must have sufficient funds and permission to transfer.
            The recipient account must be active and valid.
            """,
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Transfer completed successfully",
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
            description = "Forbidden - User does not have permission to transfer",
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
        value = "/payout",
        consumes = "application/json",
        produces = "application/json"
    )
    ResponseEntity<MoneyTransferResponse> payout(
        
        @Parameter(
            description = "Money transfer request containing transfer details",
            required = true
        )
        @RequestBody MoneyTransferRequestDto request
    );
}
