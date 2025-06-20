package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.dto.response.MoneyTransferResponse;

/**
 * This interface defines the API for payout operations in the OBS service.
 * It provides a method to process money transfer requests.
 */

public interface PayoutServiceApi {
    /**
     * Processes a money transfer request.
     *
     * @param moneyTransferRequestDto The request containing transfer details.
     * @return A MoneyTransferResponse containing the result of the transfer.
     */
    MoneyTransferResponse payout(MoneyTransferRequestDto moneyTransferRequestDto);
}
