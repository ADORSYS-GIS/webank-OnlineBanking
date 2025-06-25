package com.adorsys.webank.obs.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.adorsys.webank.exception.AccountNotFoundException;
import com.adorsys.webank.exception.ServiceUnavailableException;
import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.dto.response.BalanceResponse;
import com.adorsys.webank.obs.service.BalanceServiceApi;

import de.adorsys.webank.bank.api.domain.BalanceBO;
import de.adorsys.webank.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceServiceApi {

    private final BankAccountService bankAccountService;

    /**
     * Handles balance requests by fetching the balance for a given account ID.
     *
     * @param balanceRequest The balance request containing the account ID.
     * @return A string representing the balance or throws appropriate exceptions.
     */

    @Override
    public BalanceResponse getBalance(BalanceRequest balanceRequest) {
        String accountId = balanceRequest.getAccountID();
        try {
            BankAccountDetailsBO details = getAccountDetails(accountId);
            BalanceBO balance = getLatestBalance(details, accountId);
            return buildBalanceResponse(accountId, balance);
        } catch (Exception e) {
            throw new ServiceUnavailableException("Error retrieving balance: " + e.getMessage());
        }
    }

    private BankAccountDetailsBO getAccountDetails(String accountId) {
        BankAccountDetailsBO details = bankAccountService.getAccountDetailsById(
                accountId,
                LocalDateTime.now(),
                true
        );
        if (details == null || details.getBalances() == null || details.getBalances().isEmpty()) {
            throw new AccountNotFoundException("No balance information available for account: " + accountId);
        }
        return details;
    }

    private BalanceBO getLatestBalance(BankAccountDetailsBO details, String accountId) {
        return details.getBalances().stream().findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Balance not available for account: " + accountId));
    }

    private BalanceResponse buildBalanceResponse(String accountId, BalanceBO balance) {
        BigDecimal amount = balance.getAmount().getAmount();
        BalanceResponse response = new BalanceResponse();
        response.setAccountId(accountId);
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(BalanceResponse.BalanceStatus.AVAILABLE);
        response.setMessage("Balance retrieved successfully");
        response.setBalance(amount);
        return response;
    }
}