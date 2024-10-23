package com.adorsys.webank.obs.service;

public interface OtpServiceApi {
    /**
     * Method to receive and process the OTP.
     *
     * @param otp The OTP string received.
     */
    void receiveOtp(String otp);
}

