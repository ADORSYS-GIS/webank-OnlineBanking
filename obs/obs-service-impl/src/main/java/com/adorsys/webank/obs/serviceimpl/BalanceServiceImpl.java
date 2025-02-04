package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.service.BalanceServiceApi;
import de.adorsys.webank.bank.api.domain.BalanceBO;
import de.adorsys.webank.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BalanceServiceImpl implements BalanceServiceApi {

    private final BankAccountService bankAccountService;

    public BalanceServiceImpl(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public String getBalance(BalanceRequest request) {
        String accountId = request.getAccountID();

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
}