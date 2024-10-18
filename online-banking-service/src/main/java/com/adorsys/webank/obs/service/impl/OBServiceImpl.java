package com.adorsys.webank.obs.service.impl;

import com.adorsys.webank.obs.service.api.OBServiceAPI;
import org.springframework.stereotype.Service;

@Service
public class OBServiceImpl implements OBServiceAPI {

    @Override
    public String getMessage() {
        return "Hello from OBService!";
    }
}
