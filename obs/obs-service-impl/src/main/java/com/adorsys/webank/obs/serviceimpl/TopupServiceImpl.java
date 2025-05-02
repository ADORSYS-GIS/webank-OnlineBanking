package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.TopupRequestDto;
import com.adorsys.webank.obs.security.JwtCertValidator;
import com.adorsys.webank.obs.service.TopupServiceApi;
import de.adorsys.webank.bank.api.domain.AmountBO;
import de.adorsys.webank.bank.api.domain.BankAccountBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.BankAccountTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Currency;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopupServiceImpl implements TopupServiceApi {

    private final BankAccountService bankAccountService;
    private final BankAccountTransactionService bankAccountTransactionService;

    @Override
    @Transactional
    public String topup(TopupRequestDto topupRequestDto, String accountCertificateJwt) {

        String accountId = topupRequestDto.getAccountId();
        String amount = topupRequestDto.getAmount();
        log.info("Processing topup request for account: {}", accountId);

        try {
            if (log.isInfoEnabled()) {
                log.info("Processing transaction for accountId: {}", accountId);
            }
            try {
                // Fetch the account details
                BankAccountBO bankAccount = bankAccountService.getAccountById(accountId);
                if (bankAccount == null) {
                    if (log.isErrorEnabled()) {
                        log.error("Bank account not found for accountId: {}", accountId);
                    }
                    return "Bank account not found for ID: " + accountId;
                }

                // Define multiple deposit values
                BigDecimal[] depositValues = {
                        new BigDecimal(amount),
                };

                Currency currency = Currency.getInstance("XAF");
                String recordUser = "Default name";

                // Process each transaction
                for (BigDecimal depositValue : depositValues) {
                    AmountBO depositAmount = new AmountBO(currency, depositValue);
                    if (log.isInfoEnabled()) {
                        log.info("Processing deposit of {} for accountId: {}", depositValue, accountId);
                    }
                    bankAccountTransactionService.depositCash(accountId, depositAmount, recordUser);
                }

                return "5 transactions completed successfully for account " + accountId;

            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("An error occurred while processing the transactions for accountId: {}: {}", accountId, e.getMessage(), e);
                }
                return "An error occurred while processing the transactions: "
                        + (e.getMessage() != null ? e.getMessage() : e.toString());
            }
        } finally {

        }
    }
}