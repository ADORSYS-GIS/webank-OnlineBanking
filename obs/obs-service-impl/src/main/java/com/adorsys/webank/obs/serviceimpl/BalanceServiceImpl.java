package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.dto.*;
import com.adorsys.webank.obs.security.*;
import com.adorsys.webank.obs.service.*;
import de.adorsys.webank.bank.api.domain.*;
import de.adorsys.webank.bank.api.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class BalanceServiceImpl implements BalanceServiceApi {

    private final BankAccountService bankAccountService;


    private final JwtCertValidator jwtCertValidator;


    public BalanceServiceImpl(BankAccountService bankAccountService, JwtCertValidator jwtCertValidator) {
        this.bankAccountService = bankAccountService;
        this.jwtCertValidator = jwtCertValidator;
    }

    @Override
    public String getBalance(BalanceRequest balanceRequest, String accountCertificateJwt) {
        try {
            boolean isValid = jwtCertValidator.validateJWT(accountCertificateJwt);

            if (!isValid){
                return "Invalid certificate or JWT. Account creation failed";
            }
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