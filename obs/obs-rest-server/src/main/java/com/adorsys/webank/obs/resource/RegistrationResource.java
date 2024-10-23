/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.service.RegistrationServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationResource implements RegistrationResourceApi {

    private final RegistrationServiceApi RegistrationService;

    @Autowired
    public RegistrationResource(RegistrationServiceApi RegistrationService) {
        this.RegistrationService = RegistrationService;
    }

    @Override
    public ResponseEntity<String> registerAccount(String phoneNumber, String publicKey) {
        // Call the RegistrationServiceApi to handle the registration logic
        String response = RegistrationService.registerAccount(phoneNumber, publicKey);

        return ResponseEntity.ok(response);
    }
}
