package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.TransRequest;

public interface TransServiceApi {
    String getTrans(TransRequest transRequest, String accountCertificateJwt);
}
