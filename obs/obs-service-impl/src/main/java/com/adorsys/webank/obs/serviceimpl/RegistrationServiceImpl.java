/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.RegistrationServiceApi;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationServiceApi {

    @Override
    public String registerAccount(String phoneNumber, String publicKey) {
        // For now, we just return a successful account creation,
        //Have to implement the logic to foward the request to the module and get back a respond
        return "Successfully registered account for phone number: " + phoneNumber;
    }
}
