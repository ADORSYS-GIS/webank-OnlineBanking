package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.PayoutRequest;
import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.service.PayoutServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PayoutServiceImpl implements PayoutServiceApi {

    private static final Logger log = LoggerFactory.getLogger(PayoutServiceImpl.class);




    @Override
    public String payout(PayoutRequest payoutRequest, String accountCertificateJwt) {
        return "Success";
    }

}