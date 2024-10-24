package com.adorsys.webank.obs.service;

import jakarta.ws.rs.Consumes;

@Consumes("application/json")
public interface OtpServiceApi {
    /**
     * Method to receive and process the OTP.
     *
     * @param otp The OTP string received.
     */
    String receiveOtp(String otp);
}

