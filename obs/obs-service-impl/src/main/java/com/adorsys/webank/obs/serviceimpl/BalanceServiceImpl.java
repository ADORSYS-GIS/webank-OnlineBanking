package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.service.*;
import de.adorsys.webank.bank.api.domain.*;
import de.adorsys.webank.bank.api.service.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceServiceApi {

    private final BankAccountService bankAccountService;

    /**
     * Handles balance requests by fetching the balance for a given account ID.
     * Implements circuit breaker pattern to handle external service failures gracefully.
     *
     * @param balanceRequest The balance request containing the account ID.
     * @return A BalanceResponse containing the balance information or error details.
     */
    @Override
    @CircuitBreaker(name = "balanceService", fallbackMethod = "getBalanceFallback")
    public BalanceResponse getBalance(BalanceRequest balanceRequest) {
        log.info("Processing balance request for account: {}", balanceRequest.getAccountID());
        
        try {
            String accountId = balanceRequest.getAccountID();

            BankAccountDetailsBO details = bankAccountService.getAccountDetailsById(
                    accountId,
                    LocalDateTime.now(),
                    true
            );

            if (details == null || details.getBalances() == null || details.getBalances().isEmpty()) {
                log.warn("No balance found for account: {}", accountId);
                return BalanceResponse.error("Balance empty", accountId);
            }

            // Assuming the first balance in the list is the latest balance
            Optional<BalanceBO> latestBalance = details.getBalances().stream().findFirst();

            String balanceAmount = latestBalance.map(balanceBO -> String.valueOf(balanceBO.getAmount().getAmount()))
                    .orElse("0");
            
            log.info("Successfully retrieved balance for account: {}, balance: {}", accountId, balanceAmount);
            return BalanceResponse.success(balanceAmount, accountId);
            
        } catch (Exception e) {
            log.error("Error occurred while processing balance request for account: {}", 
                     balanceRequest.getAccountID(), e);
            throw e; // Let the circuit breaker handle this
        }
    }

    /**
     * Fallback method for circuit breaker when external service is unavailable.
     * This method is called when the circuit breaker is open or when exceptions occur.
     *
     * @param balanceRequest The balance request containing the account ID.
     * @param exception The exception that triggered the fallback.
     * @return A BalanceResponse with SERVICE_UNAVAILABLE status.
     */
    public BalanceResponse getBalanceFallback(BalanceRequest balanceRequest, Exception exception) {
        String accountId = balanceRequest.getAccountID();
        log.warn("Circuit breaker fallback triggered for account: {}. Exception: {}", 
                accountId, exception.getMessage());
        
        return BalanceResponse.serviceUnavailable(accountId);
    }
}