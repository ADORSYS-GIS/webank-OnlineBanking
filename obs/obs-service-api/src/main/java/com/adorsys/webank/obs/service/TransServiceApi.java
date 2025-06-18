package com.adorsys.webank.obs.service;

import com.adorsys.webank.obs.dto.TransRequest;

/**
 * This interface defines the API for transaction-related operations in the OBS service.
 * It provides a method to retrieve transaction details based on a specific request.
 */
public interface TransServiceApi {
    String getTrans(TransRequest transRequest);
}
