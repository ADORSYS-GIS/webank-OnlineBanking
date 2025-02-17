package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.PayoutRequest;
import com.nimbusds.jwt.SignedJWT;


public interface PayoutServiceApi {
    String payout(PayoutRequest payoutRequest, String accountCertificateJwt);
}
