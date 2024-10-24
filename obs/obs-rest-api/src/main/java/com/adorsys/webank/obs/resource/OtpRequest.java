package com.adorsys.webank.obs.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OtpRequest {
    @JsonProperty("otp")
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}