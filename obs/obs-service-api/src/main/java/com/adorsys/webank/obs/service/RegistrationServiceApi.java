package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.response.RegistrationResponse;

/**
 * This interface defines the API for account registration operations in the OBS service.
 * It provides a method to register an account using a JWT token.
 */
public interface RegistrationServiceApi {

    /**
     * Registers an account using a JWT token.
     *
     * @return A RegistrationResponse containing the result of the registration process.
     */
    RegistrationResponse registerAccount();
}