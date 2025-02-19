package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.PayoutRequest;


public interface PayoutServiceApi {
    String payout(PayoutRequest payoutRequest, String accountCertificateJwt);
}
