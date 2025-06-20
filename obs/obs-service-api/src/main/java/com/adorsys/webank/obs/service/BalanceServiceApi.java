package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.dto.response.BalanceResponse;

/**
 * Service interface for handling balance-related operations.
 */
public interface BalanceServiceApi {
    
    /**
     * Retrieves the balance for a given account.
     *
     * @param balanceRequest The balance request containing the account ID.
     * @return A BalanceResponse object containing the balance information.
     */
    BalanceResponse getBalance(BalanceRequest balanceRequest);
}
