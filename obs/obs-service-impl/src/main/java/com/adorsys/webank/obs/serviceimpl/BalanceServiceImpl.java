package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.BalanceServiceApi;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl implements BalanceServiceApi {

    @Override
    public String getBalance(String accountID) {
        // Mock implementation: Replace with actual logic to retrieve balance
        return "1000.00";
    }
}
