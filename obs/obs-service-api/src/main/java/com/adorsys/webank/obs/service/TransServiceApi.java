package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.dto.response.TransactionHistoryResponse;

/**
 * This interface defines the API for transaction-related operations in the OBS service.
 * It provides a method to retrieve transaction details based on a specific request.
 */
public interface TransServiceApi {
    /**
     * Retrieves the transaction history for a specified account.
     *
     * @param transRequest The transaction request containing the account ID.
     * @return A TransactionHistoryResponse object containing the transaction history.
     */
    TransactionHistoryResponse getTrans(TransRequest transRequest);
}
