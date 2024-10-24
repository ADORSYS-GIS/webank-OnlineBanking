package com.adorsys.webank.obs.dto;

public class RegistrationRequest {

    private int phoneNumber;
    private String publicKey;

    // Getters and Setters

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
