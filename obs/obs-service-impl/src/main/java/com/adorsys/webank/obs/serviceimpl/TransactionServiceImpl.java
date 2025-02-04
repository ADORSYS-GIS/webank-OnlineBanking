package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.TransactionServiceApi;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionServiceApi {

    @Override
    public String getTransaction(String transactionId) {
        // Mock implementation: Replace with actual logic to retrieve transaction details
        return "Transaction details for ID: " + transactionId;
    }
}
