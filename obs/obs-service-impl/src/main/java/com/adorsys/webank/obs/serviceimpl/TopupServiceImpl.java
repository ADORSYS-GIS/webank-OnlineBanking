package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.exception.AccountNotFoundException;
import com.adorsys.webank.exception.ServiceUnavailableException;
import com.adorsys.webank.obs.dto.TopupRequestDto;
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
    public String topup(TopupRequestDto topupRequestDto) {
        String accountId = topupRequestDto.getAccountId();
        String amount = topupRequestDto.getAmount();
        log.info("Processing topup request for account: {}", accountId);

        try {
            logProcessingInfo(accountId);
            validateAccountExists(accountId);
            processTopupTransaction(accountId, amount);
            return "5 transactions completed successfully for account " + accountId;
        } catch (Exception e) {
            handleTopupError(accountId, e);
            throw e;
        }
    }

    private void logProcessingInfo(String accountId) {
        if (log.isInfoEnabled()) {
            log.info("Processing transaction for accountId: {}", accountId);
        }
    }

    private void validateAccountExists(String accountId) {
        BankAccountBO bankAccount = bankAccountService.getAccountById(accountId);
        if (bankAccount == null) {
            if (log.isErrorEnabled()) {
                log.error("Bank account not found for accountId: {}", accountId);
            }
            throw new AccountNotFoundException("Bank account not found for ID: " + accountId);
        }
    }

    private void processTopupTransaction(String accountId, String amount) {
        BigDecimal[] depositValues = { new BigDecimal(amount) };
        Currency currency = Currency.getInstance("XAF");
        String recordUser = "Default name";

        for (BigDecimal depositValue : depositValues) {
            AmountBO depositAmount = new AmountBO(currency, depositValue);
            if (log.isInfoEnabled()) {
                log.info("Processing deposit of {} for accountId: {}", depositValue, accountId);
            }
            bankAccountTransactionService.depositCash(accountId, depositAmount, recordUser);
        }
    }

    private void handleTopupError(String accountId, Exception e) {
        if (log.isErrorEnabled()) {
            log.error("An error occurred while processing the transactions for accountId: {}: {}", accountId, e.getMessage(), e);
        }
        if (e instanceof AccountNotFoundException) {
            return;
        }
        throw new ServiceUnavailableException("An error occurred while processing the transactions: "
                + (e.getMessage() != null ? e.getMessage() : e.toString()));
    }
}