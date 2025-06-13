package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.*;
import com.adorsys.webank.obs.service.*;
import org.springframework.stereotype.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.adorsys.webank.config.SecurityUtils;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutServiceImpl implements PayoutServiceApi {

    private final TransactionHelper transactionHelper;

    /**
     * Handles payout requests for certified accounts.
     * Requires the user to have the ROLE_ACCOUNT_CERTIFIED or ROLE_KYC_CERT and be authenticated.
     *
     * @param moneyTransferRequestDto The request body containing payout details.
     * @return A string indicating the result of the payout processing.
     */

    @Override
    public String payout(MoneyTransferRequestDto moneyTransferRequestDto) {

        // extract the JWT token from the current context
        Optional<String> jwtOpt = SecurityUtils.getCurrentUserJWT();
        if (jwtOpt.isEmpty()) {
            throw new IllegalStateException("No JWT token found in security context");
        }
        String jwtToken = jwtOpt.get();

        log.info("jwt token from current sprint context is {}", jwtToken);

            // Extract accountCert and kycCert from the JWT header
        String accountCert = JwtHeaderExtractor.extractField(jwtToken, "accountJwt");
        log.info("Payout request: accountCert = {}", accountCert);

        String kycCert = JwtHeaderExtractor.extractField(jwtToken, "kycCertJwt");
        log.info("Payout request: kycCert = {}", kycCert);

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
                log
        );
    }
}