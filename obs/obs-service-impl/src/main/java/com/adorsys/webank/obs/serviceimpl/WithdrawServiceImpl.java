package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.security.SignTransactionJwtValidator;
import com.adorsys.webank.obs.service.WithdrawServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawServiceImpl implements WithdrawServiceApi {
    private static final Logger LOG = LoggerFactory.getLogger(WithdrawServiceImpl.class);
    private final TransactionHelper transactionHelper;
    private final SignTransactionJwtValidator signTransactionValidator;

    @Autowired
    public WithdrawServiceImpl(TransactionHelper transactionHelper,
                               SignTransactionJwtValidator signTransactionValidator) {
        this.transactionHelper = transactionHelper;
        this.signTransactionValidator = signTransactionValidator;
    }

    @Override
    public String withdraw(MoneyTransferRequestDto request, String accountCertJwt) {
        // First, verify the transaction JWT
        if (!signTransactionValidator.validateSignTransactionJWT(accountCertJwt)) {
            return "Invalid transaction JWT";
        }
        // Delegate common validation and processing to TransactionHelper
        return transactionHelper.validateAndProcessTransaction(
                request.getSenderAccountId(),
                request.getRecipientAccountId(),
                request.getAmount(),
                accountCertJwt,
                LOG
        );
    }
}
