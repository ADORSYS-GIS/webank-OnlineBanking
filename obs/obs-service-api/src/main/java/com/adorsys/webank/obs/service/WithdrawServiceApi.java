package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.MoneyTransferRequestDto;
import com.adorsys.webank.obs.dto.response.MoneyTransferResponse;
import org.springframework.stereotype.Service;

/**
 * Service interface for handling withdrawal operations.
 * This interface defines the contract for processing withdrawal requests
 * and managing the removal of funds from user accounts.
 */
@Service
public interface WithdrawServiceApi {
    /**
     * Processes a withdrawal request to remove funds from a user's account.
     * This method handles the business logic for withdrawals, including:
     * - Validating the request parameters
     * - Checking account balance and limits
     * - Processing the withdrawal
     * - Generating appropriate response
     *
     * @param moneyTransferRequestDto The withdrawal request containing account and amount details
     * @return MoneyTransferResponse containing the result of the operation with:
     *         - Status (COMPLETED, PENDING, INSUFFICIENT_FUNDS, or FAILED)
     *         - Transaction details
     *         - Response message
     */
    MoneyTransferResponse withdraw(MoneyTransferRequestDto moneyTransferRequestDto);
}
