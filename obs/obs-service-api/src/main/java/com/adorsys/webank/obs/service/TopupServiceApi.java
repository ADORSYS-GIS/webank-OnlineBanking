package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.TopupRequestDto;
import com.adorsys.webank.obs.dto.response.TopupResponse;

/**
 * Service interface for handling account top-up operations.
 * This interface defines the contract for processing top-up requests
 * and managing the addition of funds to user accounts.
 */
public interface TopupServiceApi {
    
    /**
     * Processes a top-up request to add funds to a user's account.
     * This method handles the business logic for adding funds, including:
     * - Validating the request parameters
     * - Checking account status and limits
     * - Processing the transaction
     * - Generating appropriate response
     *
     * @param topupRequestDto The top-up request containing account ID and amount details
     * @return TopupResponse containing the result of the operation with:
     *         - Status (COMPLETED, PENDING, INVALID_AMOUNT, or FAILED)
     *         - Transaction details
     *         - Response message
     */
    TopupResponse topup(TopupRequestDto topupRequestDto);
} 