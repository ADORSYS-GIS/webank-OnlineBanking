package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TransRequest;
import com.adorsys.webank.obs.service.TransServiceApi;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/accounts/balance")
public class TransRest implements TransRestApi {

    private final TransServiceApi transService;

    @Autowired
    public TransRest(TransServiceApi transService) {

        this.transService = transService;
    }

    @Override
    public String getTrans( String authorizationHeader, TransRequest request) {
        return transService.getTrans(request.getAccountID());
    }
}