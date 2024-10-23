package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.service.OtpServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpRestServer implements OtpRestApi {

    private final OtpServiceApi otpServiceApi;

    @Autowired
    public OtpRestServer(OtpServiceApi otpServiceApi) {
        this.otpServiceApi = otpServiceApi;
    }

    @Override
    public ResponseEntity<String> receiveOtp(String otp) {
        try {
            otpServiceApi.receiveOtp(otp);
            return new ResponseEntity<>("OTP received and processed successfully.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to process OTP.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

