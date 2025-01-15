package com.adorsys.webank.obs.resource;


import com.adorsys.webank.obs.dto.OtpValidationRequest;
import com.adorsys.webank.obs.service.ObsOtpServiceApi;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.adorsys.webank.obs.dto.OtpRequest;


@RestController
public class ObsOtpRestServer implements ObsOtpRestApi {
    private final ObsOtpServiceApi otpService;

    public ObsOtpRestServer(ObsOtpServiceApi otpService) {

        this.otpService = otpService;
    }

//    @Override
//    public String sendOtp(OtpRequest request) {
//        return otpService.sendOtp(request.getPhoneNumber(),request.getPublicKey());
//    }

    @Override
    public ResponseEntity<String> sendOtp(OtpRequest request) {
        try {
            // Try to send OTP
            String otpResponse = otpService.sendOtp(request.getPhoneNumber(), request.getPublicKey());
            return ResponseEntity.ok(otpResponse);  // Successfully sent OTP
        } catch (IllegalArgumentException e) {
            // Handle error: OTP already sent recently for this phone number
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  // 400 Bad Request
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected server error occurred.");
        }
    }

    @Override
    public String validateOtp(OtpValidationRequest request) {
        return otpService.validateOtp(request.getPhoneNumber(),  request.getPublicKey() ,request.getOtpInput(), request.getOtpHash() );
    }
}
