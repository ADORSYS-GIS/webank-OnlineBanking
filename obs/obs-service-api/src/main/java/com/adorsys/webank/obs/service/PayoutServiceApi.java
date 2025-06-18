package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;

/**
 * This interface defines the API for payout operations in the OBS service.
 * It provides a method to process money transfer requests.
 */

public interface PayoutServiceApi {
    String payout(MoneyTransferRequestDto moneyTransferRequestDto);
}
