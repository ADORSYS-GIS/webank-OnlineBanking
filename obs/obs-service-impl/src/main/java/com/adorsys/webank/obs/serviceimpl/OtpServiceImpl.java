package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.OtpServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpServiceApi {
    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    @Override
    public String sendOtp(String phoneNumber, String publicKey) {

        return "OTP successfully received";
    }
    @Override
    public boolean validateOtp(String phoneNumber, String publicKey, String otpInput , String otpHash){

        return true;
    }
}


