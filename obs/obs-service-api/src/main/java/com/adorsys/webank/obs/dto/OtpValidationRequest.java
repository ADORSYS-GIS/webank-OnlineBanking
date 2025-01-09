package com.adorsys.webank.obs.dto;

public class OtpValidationRequest {
    private String phoneNumber;
    private String otpInput;
    private String otpHash;
    private String publicKey;

    public OtpValidationRequest() {
    }

    public OtpValidationRequest(String phoneNumber, String otpInput, String otpHash, String publicKey) {
        this.phoneNumber = phoneNumber;
        this.otpInput = otpInput;
        this.otpHash = otpHash;
        this.publicKey = publicKey;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public String getOtpInput() {
        return otpInput;
    }
}
