//package com.adorsys.webank.obs.resource;
//
//import com.adorsys.webank.obs.dto.RegistrationRequest;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/registration")
//public interface RegistrationResourceApi {
//
//    @Operation(summary = "Register a new bank account", description = "Accepts a phone number and public key for registration")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Registration successful"),
//            @ApiResponse(responseCode = "400", description = "Invalid input"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    @PostMapping
//    ResponseEntity<String> registerAccount(@RequestBody RegistrationRequest registrationRequest);
//}
