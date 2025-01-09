package com.adorsys.webank.obs.resource;


import com.adorsys.webank.obs.dto.OtpRequest;
import com.adorsys.webank.obs.dto.OtpValidationRequest;
import com.adorsys.webank.obs.service.ObsOtpServiceApi;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ObsOtpRestServer implements ObsOtpRestApi {
    private final ObsOtpServiceApi otpService;

    public ObsOtpRestServer(ObsOtpServiceApi otpService) {

        this.otpService = otpService;
    }

    @Override
    public String sendOtp(OtpRequest request) {
        return otpService.sendOtp(request.getPhoneNumber(),request.getPublicKey());
    }

    @Override
    public boolean validateOtp(OtpValidationRequest request) {
        return otpService.validateOtp(request.getPhoneNumber(),  request.getPublicKey() ,request.getOtpInput(), request.getOtpHash() );
    }
}
