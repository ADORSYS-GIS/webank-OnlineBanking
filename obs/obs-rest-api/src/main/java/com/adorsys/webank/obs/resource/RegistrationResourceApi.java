package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.response.ErrorResponse;
import com.adorsys.webank.obs.dto.response.RegistrationResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registration")
@Tag(
    name = "Account Registration",
    description = "APIs for managing bank account registration process"
)
public interface RegistrationResourceApi {

    @Operation(
        summary = "Register a new bank account",
        description = """
            Registers a new bank account using the provided JWT token in the Authorization header.
            The JWT token must contain a valid public key that will be used for account operations.
            This endpoint initiates the account registration process and returns a registration ID.
            """,
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Account registration initiated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegistrationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request - Missing or invalid authorization header",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid JWT token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict - Account already registered",
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
    @PostMapping
    ResponseEntity<RegistrationResponse> registerAccount(
    );
}
