/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.OBSServiceApi;
import org.springframework.stereotype.Service;

@Service
public class OBSServiceImpl implements OBSServiceApi {

    @Override
    public String registerAccount(String phoneNumber, String publicKey) {
        // For now, just simulate forwarding the request
        return "Successfully registered account for phone number: " + phoneNumber;
    }
}
