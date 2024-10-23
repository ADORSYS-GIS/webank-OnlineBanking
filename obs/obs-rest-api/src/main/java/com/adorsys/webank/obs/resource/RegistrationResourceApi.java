/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package com.adorsys.webank.obs.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public interface RegistrationResourceApi {

    @Operation(summary = "Register a new bank account", description = "Accepts a phone number and public key for registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    ResponseEntity<String> registerAccount(
            @Parameter(required = true)
            @RequestParam("phoneNumber") String phoneNumber,

            @Parameter(required = true)
            @RequestParam("publicKey") String publicKey
    );
}
