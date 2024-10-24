package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.RegistrationRequest;
import com.adorsys.webank.obs.service.RegistrationServiceApi;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationServiceApi {

    @Override
    public String registerAccount(RegistrationRequest registrationRequest) {
        int phoneNumber = registrationRequest.getPhoneNumber();
        String publicKey = registrationRequest.getPublicKey();

        // Add logic for registering the account with phoneNumber and publicKey

        return "Registration successful for phone number: " + phoneNumber; // Return message including the phone number
    }
}
