package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.BalanceRequest;

/**
 * This interface defines the API for balance-related operations in the OBS service.
 * It provides methods to retrieve account balances based on specific requests.
 */
public interface BalanceServiceApi {
    String getBalance(BalanceRequest balanceRequest);
}
