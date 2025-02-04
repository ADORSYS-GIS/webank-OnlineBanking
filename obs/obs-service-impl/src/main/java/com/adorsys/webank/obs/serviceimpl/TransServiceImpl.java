package com.adorsys.webank.obs.serviceimpl;

import com.adorsys.webank.obs.service.TransServiceApi;
import org.springframework.stereotype.Service;

@Service
public class TransServiceImpl implements TransServiceApi {

    @Override
    public String getTrans(String accountID) {
        // Mock implementation: Replace with actual logic to retrieve trans details
        return "Trans details for ID: " + accountID;
    }
}
