package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.service.PayoutServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PayoutServiceImpl implements PayoutServiceApi {

    private static final Logger log = LoggerFactory.getLogger(PayoutServiceImpl.class);




    @Override
    public String payout(TransRequest transRequest, String accountCertificateJwt) {
        return "Success";
    }

}