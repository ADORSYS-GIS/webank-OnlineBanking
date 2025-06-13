package com.adorsys.webank.obs.serviceimpl;

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
     * @return A string representing the balance or an error message.
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
                return "Balance empty";
            }

            // Assuming the first balance in the list is the latest balance
            Optional<BalanceBO> latestBalance = details.getBalances().stream().findFirst();

            return latestBalance.map(balance -> String.valueOf(balance.getAmount().getAmount()))
                    .orElse("Balance not available");
        }
        catch (Exception e) {
            return "An error occurred while processing the request: " + e.getMessage();
        }


    }



}