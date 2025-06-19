package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.dto.response.MoneyTransferResponse;
import com.adorsys.webank.obs.security.JwtHeaderExtractor;
import com.adorsys.webank.obs.service.PayoutServiceApi;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.adorsys.webank.config.SecurityUtils;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutServiceImpl implements PayoutServiceApi {

    private final TransactionHelper transactionHelper;

    @Override
    public MoneyTransferResponse payout(MoneyTransferRequestDto moneyTransferRequestDto) {
        String jwtToken = extractJwtToken();

        log.info("Extracting account and KYC certificates from JWT.");
        String accountCert = extractCertificate(jwtToken, "accountJwt");
        String kycCert = extractCertificate(jwtToken, "kycCertJwt");

        validateAccountCertificate(accountCert);
        validateKycCertificateForLargeTransaction(moneyTransferRequestDto, kycCert);

        String result = transactionHelper.validateAndProcessTransaction(
                moneyTransferRequestDto.getSenderAccountId(),
                moneyTransferRequestDto.getRecipientAccountId(),
                moneyTransferRequestDto.getAmount().toPlainString(),
                jwtToken
        );

        MoneyTransferResponse response = new MoneyTransferResponse();
        response.setStatus(MoneyTransferResponse.TransferStatus.COMPLETED);
        response.setTransactionId(result);
        response.setAmount(moneyTransferRequestDto.getAmount());
        response.setCurrency("XAF");
        response.setTimestamp(LocalDateTime.now());
        response.setMessage("Transfer completed successfully");
        
        return response;
    }

    private String extractJwtToken() {
        Optional<String> jwtOpt = SecurityUtils.getCurrentUserJWT();
        if (jwtOpt.isEmpty()) {
            throw new IllegalStateException("No JWT token found in security context");
        }
        return jwtOpt.get();
    }

    private String extractCertificate(String jwtToken, String headerField) {
        return JwtHeaderExtractor.extractField(jwtToken, headerField);
    }

    private void validateAccountCertificate(String accountCert) {
        if (accountCert == null || accountCert.isEmpty()) {
            throw new IllegalArgumentException("Account certificate is required for all transactions.");
        }
    }

    private void validateKycCertificateForLargeTransaction(MoneyTransferRequestDto moneyTransferRequestDto, String kycCert) {
        double amount = moneyTransferRequestDto.getAmount().doubleValue();
        if (amount > 1000 && (kycCert == null || kycCert.isEmpty())) {
            throw new IllegalArgumentException("KYC certificate is required for transactions exceeding 1,000 francs.");
        }
    }
}