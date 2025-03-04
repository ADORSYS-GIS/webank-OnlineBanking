package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface WithdrawServiceApi {
    String withdraw(MoneyTransferRequestDto moneyTransferRequestDto, String accountCertificateJwt);
}
