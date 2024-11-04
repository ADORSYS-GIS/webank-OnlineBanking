package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.service.OnlineBankingServiceApi;
import de.adorsys.ledgers.bank.api.domain.account.AccountDetailsTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/obs/accounts")
public class BankingResource {
    private final OnlineBankingServiceApi onlineBankingService;

    public BankingResource(OnlineBankingServiceApi onlineBankingService) {
        this.onlineBankingService = onlineBankingService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDetailsTO> createDepositAccount(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody AccountDetailsTO accountDetails) {
        AccountDetailsTO createdAccount = onlineBankingService.createDepositAccount(authorizationHeader, accountDetails);
        return ResponseEntity.ok(createdAccount);
    }
}
