package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.RecoveryDto;
import com.adorsys.webank.obs.dto.response.ErrorResponse;
import com.adorsys.webank.obs.dto.response.RecoveryResponse;
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
    name = "Account Recovery",
    description = "APIs for managing account recovery processes"
)
@RequestMapping("/api/accounts")
public interface RecoveryRestApi {

    @Operation(
        summary = "Initiate account recovery",
        description = """
            Initiates the account recovery process for a locked or compromised account.
            This process may involve additional verification steps and security checks.
            The recovery process is time-sensitive and may require multiple steps to complete.
            """,
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Recovery process initiated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RecoveryResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request - Missing or invalid recovery information",
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
            responseCode = "404",
            description = "Account not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Recovery already in progress",
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
        value = "/recovery",
        consumes = "application/json",
        produces = "application/json"
    )
    ResponseEntity<RecoveryResponse> recoverAccount(
        @Parameter(
            description = "JWT token in the format 'Bearer <token>'",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
        
        @Parameter(
            description = "Recovery request containing account recovery details",
            required = true
        )
        @RequestBody RecoveryDto recoveryDto
    );
}
