package com.adorsys.webank.obs;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class OBSRestServer implements OBSRestApi {

    private final OBSServiceApi obsService;

    @Autowired
    public OBSRestServer(OBSServiceApi obsService) {
        this.obsService = obsService;
    }

    @Override
    public String getMessage() {
        return obsService.getMessage();
    }
}

