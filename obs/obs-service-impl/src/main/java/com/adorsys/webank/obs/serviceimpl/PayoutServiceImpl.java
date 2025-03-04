package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.PayoutRequest;
import com.adorsys.webank.obs.service.PayoutServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayoutServiceImpl implements PayoutServiceApi {
    private static final Logger LOG = LoggerFactory.getLogger(PayoutServiceImpl.class);

    private final TransactionHelper transactionHelper;

    @Autowired
    public PayoutServiceImpl(TransactionHelper transactionHelper) {
        this.transactionHelper = transactionHelper;
    }

    @Override
    public String payout(PayoutRequest request, String accountCertJwt) {
        return transactionHelper.validateAndProcessTransaction(
                request.getSenderAccountId(),
                request.getRecipientAccountId(),
                request.getAmount(),
                accountCertJwt,
                LOG
        );
    }
}
