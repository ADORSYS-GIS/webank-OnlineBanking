package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.RecoveryDto;
import org.springframework.stereotype.Service;

@Service
public interface RecoveryServiceApi {

    String recoverAccount(String devPublicKey, String accountId);
}
