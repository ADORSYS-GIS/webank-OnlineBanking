package com.adorsys.webank.obs;

import org.springframework.stereotype.Service;

@Service
public class OBSServiceImpl implements OBSServiceApi {

    @Override
    public String getMessage() {
        return "Hello from the OBS Service!";
    }
}
