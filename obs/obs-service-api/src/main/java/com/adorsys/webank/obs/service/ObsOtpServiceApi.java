package com.adorsys.webank.obs.service;
import org.springframework.stereotype.Service;

@Service
public interface ObsOtpServiceApi {
    String sendOtp(String phoneNumber, String publicKey);

    String validateOtp(String phoneNumber, String publicKey, String otpInput , String otpHash);

}
