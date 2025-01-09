package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.ObsOtpServiceApi;
import com.adorsys.webank.service.OtpServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObsOtpServiceImpl implements ObsOtpServiceApi {

    @Autowired
    private OtpServiceApi otpServiceApi;

    @Override
    public String sendOtp(String phoneNumber, String publicKey) {

        return otpServiceApi.sendOtp(phoneNumber, publicKey);
    }
    @Override
    public boolean validateOtp(String phoneNumber, String publicKey, String otpInput , String otpHash){

        return otpServiceApi.validateOtp(phoneNumber, publicKey, otpInput, otpHash);

    }
}


