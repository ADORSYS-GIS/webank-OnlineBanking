package com.adorsys.webank.obs.service;

import de.adorsys.ledgers.bank.api.domain.account.AccountDetailsTO;

public interface OnlineBankingServiceApi {
    AccountDetailsTO createDepositAccount(String authorizationHeader, AccountDetailsTO accountDetails);
}
