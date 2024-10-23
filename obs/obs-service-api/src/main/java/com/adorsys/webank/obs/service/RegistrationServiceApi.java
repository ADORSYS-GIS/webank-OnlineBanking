/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package com.adorsys.webank.obs.service;

public interface RegistrationServiceApi {
    String registerAccount(String phoneNumber, String publicKey);
}
