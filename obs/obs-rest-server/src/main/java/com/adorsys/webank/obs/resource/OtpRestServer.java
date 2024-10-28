package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.OtpRequest;
import com.adorsys.webank.obs.service.OtpServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpRestServer implements OtpRestApi {

    private final OtpServiceApi otpServiceApi;

    @Autowired
    public OtpRestServer(OtpServiceApi otpServiceApi) {
        this.otpServiceApi = otpServiceApi;
    }

    @Override
    public ResponseEntity<?> receiveOtp(@RequestBody OtpRequest otpRequest) {
        try {
            // Delegate OTP validation and processing to the service
            String responseMessage = otpServiceApi.receiveOtp(otpRequest.getOtp());

            return ResponseEntity.ok(Map.of("message", responseMessage));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
