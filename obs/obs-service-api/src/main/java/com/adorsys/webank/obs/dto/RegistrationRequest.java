package com.adorsys.webank.obs.dto;

public class RegistrationRequest {


    private String publicKey;

    private String phoneNumber;

    public RegistrationRequest(String testPhoneNumber, String testPublicKey) {
    }

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
