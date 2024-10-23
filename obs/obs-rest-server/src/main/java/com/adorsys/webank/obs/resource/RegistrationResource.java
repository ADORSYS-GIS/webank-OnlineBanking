/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.service.OBSServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OBSRestServer implements RegistrationApi {

    private final OBSServiceApi obsService;

    @Autowired
    public OBSRestServer(OBSServiceApi obsService) {
        this.obsService = obsService;
    }

    @Override
    public ResponseEntity<String> registerAccount(String phoneNumber, String publicKey) {
        // Call the OBSServiceApi to handle the registration logic
        // For now, let's just simulate a successful registration response
        String response = obsService.registerAccount(phoneNumber, publicKey);

        return ResponseEntity.ok(response);
    }
}
