package com.adorsys.webank.obs.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OTP", description = "Operations related to OTP processing")
@RequestMapping("/api/v1/otp")
public interface OtpRestApi {

    /**
     * Endpoint to receive the OTP.
     *
     * @param otp The OTP string.
     * @return ResponseEntity indicating the result of the operation.
     */
    @Operation(summary = "Receive OTP", description = "receives OTP and processes it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP successfully captured"),
            @ApiResponse(responseCode = "400", description = "OTP capture failed")
    })
    @PostMapping("/receive")
    ResponseEntity<String> receiveOtp(
            @Parameter(required = true)
            @RequestParam("OTP") String otp
    );
}
