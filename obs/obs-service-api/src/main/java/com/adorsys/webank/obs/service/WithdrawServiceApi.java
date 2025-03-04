package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.WithdrawRequest;
import org.springframework.stereotype.Service;

@Service
public interface WithdrawServiceApi {
    String withdraw(WithdrawRequest withdrawRequest, String accountCertificateJwt);
}
