package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.OtpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OTP", description = "Operations related to OTP processing")
@RequestMapping("/api/otp")
public interface OtpRestApi {

    @Operation(summary = "Receive OTP", description = "Receives OTP and processes it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP successfully captured"),
            @ApiResponse(responseCode = "400", description = "OTP capture failed")
    })
    @PostMapping(value = "/receive", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> receiveOtp(@RequestBody OtpRequest otpRequest);
}

