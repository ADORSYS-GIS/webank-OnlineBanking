package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.OnlineBankingServiceApi;
import de.adorsys.ledgers.bank.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.bank.api.resource.AccountMgmResourceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnlineBankingServiceImpl implements OnlineBankingServiceApi {
    private final AccountMgmResourceAPI accountMgmResourceAPI;

    @Autowired
    public OnlineBankingServiceImpl(AccountMgmResourceAPI accountMgmResourceAPI) {
        this.accountMgmResourceAPI = accountMgmResourceAPI;
    }

    @Override
    public AccountDetailsTO createDepositAccount(String authorizationHeader, AccountDetailsTO accountDetails) {
        return accountMgmResourceAPI.createBankAccount(authorizationHeader, accountDetails).getBody();
    }
}
