package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.exception.AccountNotFoundException;
import com.adorsys.webank.exception.ServiceUnavailableException;
import com.adorsys.webank.obs.dto.TopupRequestDto;
import com.adorsys.webank.obs.dto.response.TopupResponse;
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
    public TopupResponse topup(TopupRequestDto topupRequestDto) {
        String accountId = topupRequestDto.getAccountId();
        BigDecimal amount = topupRequestDto.getAmount();
        log.info("Processing topup request for account: {}", accountId);

            try {
                // Fetch the account details
                BankAccountBO bankAccount = bankAccountService.getAccountById(accountId);
                if (bankAccount == null) {
                        log.error("Bank account not found for accountId: {}", accountId);
                return createErrorResponse(accountId, amount, "Bank account not found");
                }

            // Process the transaction
                Currency currency = Currency.getInstance("XAF");
            AmountBO depositAmount = new AmountBO(currency, amount);
            bankAccountTransactionService.depositCash(accountId, depositAmount, "System");

            // Create success response
            TopupResponse response = new TopupResponse();
            response.setAccountId(accountId);
            response.setAmount(amount);
            response.setCurrency("XAF");
            response.setStatus(TopupResponse.TopupStatus.COMPLETED);
            response.setTransactionId(String.valueOf(System.currentTimeMillis()));
            response.setMessage("Top-up completed successfully");

            log.info("Top-up completed successfully for account: {}", accountId);
            return response;

            } catch (Exception e) {
            log.error("Error processing top-up for accountId: {}: {}", accountId, e.getMessage(), e);
            return createErrorResponse(accountId, amount, "Error processing top-up: " + e.getMessage());
        }
    }

    private TopupResponse createErrorResponse(String accountId, BigDecimal amount, String errorMessage) {
        TopupResponse response = new TopupResponse();
        response.setAccountId(accountId);
        response.setAmount(amount);
        response.setCurrency("XAF");
        response.setStatus(TopupResponse.TopupStatus.FAILED);
        response.setMessage(errorMessage);
        return response;
    }
}