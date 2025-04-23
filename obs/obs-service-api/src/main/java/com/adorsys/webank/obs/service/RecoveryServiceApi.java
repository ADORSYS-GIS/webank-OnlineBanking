package com.adorsys.webank.obs.service;

import org.springframework.stereotype.*;

@Service
public interface RecoveryServiceApi {

    String recoverAccount(String devPublicKey, String accountId);
}
