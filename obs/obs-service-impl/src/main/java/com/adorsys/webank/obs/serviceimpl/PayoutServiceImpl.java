package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.*;
import com.adorsys.webank.obs.service.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class PayoutServiceImpl implements PayoutServiceApi {
    private static final Logger LOG = LoggerFactory.getLogger(PayoutServiceImpl.class);

    private final TransactionHelper transactionHelper;

    @Autowired
    public PayoutServiceImpl(TransactionHelper transactionHelper) {
        this.transactionHelper = transactionHelper;
    }

    @Override
    public String payout(MoneyTransferRequestDto moneyTransferRequestDto, String jwtToken) {
        // Validate the JWT token
        LOG.info("JWT TOKEN IS: {}", jwtToken);

        // Extract accountCert and kycCert from the JWT header
        String accountCert = JwtHeaderExtractor.extractField(jwtToken, "accountJwt");
        LOG.info("Payout request: accountCert = {}", accountCert);

        String kycCert = JwtHeaderExtractor.extractField(jwtToken, "kycCertJwt");
        LOG.info("Payout request: kycCert = {}", kycCert);

        // Validate the presence of accountCert
        if (accountCert == null || accountCert.isEmpty()) {
            throw new IllegalArgumentException("Account certificate is required for all transactions.");
        }

        // Parse the transaction amount
        double amount = Double.parseDouble(moneyTransferRequestDto.getAmount());

        // Validate based on the transaction amount
        if (amount > 1000 && (kycCert == null || kycCert.isEmpty())) {
            throw new IllegalArgumentException("KYC certificate is required for transactions exceeding 10,000 francs.");
        }

        // Proceed with the transaction
        return transactionHelper.validateAndProcessTransaction(
                moneyTransferRequestDto.getSenderAccountId(),
                moneyTransferRequestDto.getRecipientAccountId(),
                moneyTransferRequestDto.getAmount(),
                jwtToken,
                LOG
        );
    }
}