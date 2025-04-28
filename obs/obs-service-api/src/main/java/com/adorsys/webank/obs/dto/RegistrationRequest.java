package com.adorsys.webank.obs.dto;

public class RegistrationRequest {

    private String publicKey;

    public RegistrationRequest(String testPublicKey) {
        this.publicKey = testPublicKey;
    }

    /**
     * Default constructor for RegistrationRequest.
     */
    public RegistrationRequest() {
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
