package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.dto.response.BalanceResponse;
import com.adorsys.webank.obs.service.BalanceServiceApi;
import de.adorsys.webank.bank.api.domain.BalanceBO;
import de.adorsys.webank.bank.api.domain.BankAccountDetailsBO;
import de.adorsys.webank.bank.api.service.BankAccountService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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
    public BalanceResponse getBalance(BalanceRequest balanceRequest) {
        try {
            String accountId = balanceRequest.getAccountID();

            BankAccountDetailsBO details = bankAccountService.getAccountDetailsById(
                    accountId,
                    LocalDateTime.now(),
                    true
            );

            BalanceResponse response = new BalanceResponse();
            response.setAccountId(accountId);
            response.setTimestamp(LocalDateTime.now());

            if (details == null || details.getBalances() == null || details.getBalances().isEmpty()) {
                response.setStatus(BalanceResponse.BalanceStatus.INSUFFICIENT_FUNDS);
                response.setMessage("No balance information available");
                response.setBalance(BigDecimal.ZERO);
                return response;
            }

            Optional<BalanceBO> latestBalance = details.getBalances().stream().findFirst();

            if (latestBalance.isPresent()) {
                BalanceBO balance = latestBalance.get();
                BigDecimal amount = balance.getAmount().getAmount();
                response.setStatus(BalanceResponse.BalanceStatus.AVAILABLE);
                response.setMessage("Balance retrieved successfully");
                response.setBalance(amount);
                return response;
            }

            response.setStatus(BalanceResponse.BalanceStatus.INSUFFICIENT_FUNDS);
            response.setMessage("Balance not available");
            response.setBalance(BigDecimal.ZERO);
            return response;

        } catch (Exception e) {
            BalanceResponse response = new BalanceResponse();
            response.setAccountId(balanceRequest.getAccountID());
            response.setStatus(BalanceResponse.BalanceStatus.SYSTEM_ERROR);
            response.setMessage("Error retrieving balance: " + e.getMessage());
            response.setTimestamp(LocalDateTime.now());
            response.setBalance(BigDecimal.ZERO);
            return response;
        }
    }



}