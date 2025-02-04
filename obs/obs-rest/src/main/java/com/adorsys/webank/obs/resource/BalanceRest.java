package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.BalanceRequest;
import com.adorsys.webank.obs.service.BalanceServiceApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/accounts/balance")
public class BalanceRest implements BalanceRestApi {

    private final BalanceServiceApi balanceService;

    @Autowired
    public BalanceRest(BalanceServiceApi balanceService) {
        this.balanceService = balanceService;
    }

    @Override
    public String getBalance( String authorizationHeader, BalanceRequest request) {
        return balanceService.getBalance(request.getAccountID());
    }
}
