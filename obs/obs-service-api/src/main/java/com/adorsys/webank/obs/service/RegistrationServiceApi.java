package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.RegistrationRequest;

public interface RegistrationServiceApi {
    String registerAccount(RegistrationRequest registrationRequest, String phoneNumberCertificateJwt);
}
