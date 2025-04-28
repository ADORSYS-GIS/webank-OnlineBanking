package com.adorsys.webank.obs.service;

public interface RegistrationServiceApi {
    String registerAccount(String publicKey, String registrationJwt);
}