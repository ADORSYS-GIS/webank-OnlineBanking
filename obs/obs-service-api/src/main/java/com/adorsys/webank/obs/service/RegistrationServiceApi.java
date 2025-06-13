package com.adorsys.webank.obs.service;

/**
 * This interface defines the API for account registration operations in the OBS service.
 * It provides a method to register an account using a JWT token.
 */
public interface RegistrationServiceApi {
    String registerAccount(String registrationJwt);
}