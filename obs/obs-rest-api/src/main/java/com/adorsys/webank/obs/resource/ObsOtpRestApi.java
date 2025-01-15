package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.OtpValidationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import  org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import com.adorsys.webank.obs.dto.OtpRequest;

@Tag(name = "OTP", description = "Operations related to OTP processing")
@RequestMapping("/api/otp")
public interface ObsOtpRestApi {

    @Operation(summary = "Send OTP", description = "Sends an OTP to the user's phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP successfully sent"),
            @ApiResponse(responseCode = "400", description = "Invalid phone number or OTP recently sent"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
    ResponseEntity<String> sendOtp(@RequestBody OtpRequest request);

    @Operation(summary = "Validate OTP", description = "Validates the received OTP against the stored value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP successfully validated"),
            @ApiResponse(responseCode = "400", description = "Invalid OTP"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PostMapping(value = "/validate", consumes = "application/json", produces = "application/json")
    String validateOtp(@RequestBody OtpValidationRequest request);
}


