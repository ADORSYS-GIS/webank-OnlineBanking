package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.dto.BalanceResponse;

/**
 * This interface defines the API for balance-related operations in the OBS service.
 * It provides methods to retrieve account balances based on specific requests.
 */
public interface BalanceServiceApi {
    BalanceResponse getBalance(BalanceRequest balanceRequest);
}
