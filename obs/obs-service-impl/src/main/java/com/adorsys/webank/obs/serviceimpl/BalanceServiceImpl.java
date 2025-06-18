package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.exception.AccountNotFoundException;
import com.adorsys.webank.exception.ServiceUnavailableException;
import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.service.*;
import de.adorsys.webank.bank.api.domain.*;
import de.adorsys.webank.bank.api.service.*;
import org.springframework.stereotype.*;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.*;

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
    public String getBalance(BalanceRequest balanceRequest) {
        try {
            String accountId = balanceRequest.getAccountID();

            BankAccountDetailsBO details = bankAccountService.getAccountDetailsById(
                    accountId,
                    LocalDateTime.now(),
                    true
            );

            if (details == null || details.getBalances() == null || details.getBalances().isEmpty()) {
                throw new AccountNotFoundException("No balance information available for account: " + accountId);
            }

            // Assuming the first balance in the list is the latest balance
            Optional<BalanceBO> latestBalance = details.getBalances().stream().findFirst();

            return latestBalance.map(balance -> String.valueOf(balance.getAmount().getAmount()))
                    .orElseThrow(() -> new AccountNotFoundException("Balance not available for account: " + accountId));
        }
        catch (Exception e) {
            if (e instanceof AccountNotFoundException) {
                throw e;
            }
            throw new ServiceUnavailableException("An error occurred while processing the request: " + e.getMessage());
        }
    }
}