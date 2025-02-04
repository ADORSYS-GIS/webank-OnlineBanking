package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.BalanceRequest;

public interface BalanceServiceApi {
    String getBalance(BalanceRequest balanceRequest, String accountCertificateJwt);
}
