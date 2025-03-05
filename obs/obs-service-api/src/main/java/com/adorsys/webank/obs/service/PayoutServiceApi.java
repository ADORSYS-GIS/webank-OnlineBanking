package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;


public interface PayoutServiceApi {
    String payout(MoneyTransferRequestDto moneyTransferRequestDto, String accountCertificateJwt);
}
