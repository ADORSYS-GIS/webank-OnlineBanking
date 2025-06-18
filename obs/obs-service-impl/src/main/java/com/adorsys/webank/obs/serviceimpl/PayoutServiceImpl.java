package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.security.JwtHeaderExtractor;
import com.adorsys.webank.obs.service.PayoutServiceApi;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.adorsys.webank.config.SecurityUtils;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutServiceImpl implements PayoutServiceApi {

    private final TransactionHelper transactionHelper;

    @Override
    public String payout(MoneyTransferRequestDto moneyTransferRequestDto) {
        String jwtToken = extractJwtToken();
        log.info("jwt token from current sprint context is {}", jwtToken);

        String accountCert = extractAndLogCertificate(jwtToken, "accountJwt", "Payout request: accountCert = {}");
        String kycCert = extractAndLogCertificate(jwtToken, "kycCertJwt", "Payout request: kycCert = {}");

        validateAccountCertificate(accountCert);
        validateKycCertificateForLargeTransaction(moneyTransferRequestDto, kycCert);

        return transactionHelper.validateAndProcessTransaction(
                moneyTransferRequestDto.getSenderAccountId(),
                moneyTransferRequestDto.getRecipientAccountId(),
                moneyTransferRequestDto.getAmount(),
                jwtToken,
                log
        );
    }

    private String extractJwtToken() {
        Optional<String> jwtOpt = SecurityUtils.getCurrentUserJWT();
        if (jwtOpt.isEmpty()) {
            throw new IllegalStateException("No JWT token found in security context");
        }
        return jwtOpt.get();
    }

    private String extractAndLogCertificate(String jwtToken, String headerField, String logMessage) {
        String cert = JwtHeaderExtractor.extractField(jwtToken, headerField);
        log.info(logMessage, cert);
        return cert;
    }

    private void validateAccountCertificate(String accountCert) {
        if (accountCert == null || accountCert.isEmpty()) {
            throw new IllegalArgumentException("Account certificate is required for all transactions.");
        }
    }

    private void validateKycCertificateForLargeTransaction(MoneyTransferRequestDto moneyTransferRequestDto, String kycCert) {
        double amount = Double.parseDouble(moneyTransferRequestDto.getAmount());
        if (amount > 1000 && (kycCert == null || kycCert.isEmpty())) {
            throw new IllegalArgumentException("KYC certificate is required for transactions exceeding 1,000 francs.");
        }
    }
}