package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TransactionRequest;
import com.adorsys.webank.obs.service.TransactionServiceApi;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/obs/Transaction")
public class TransactionRest implements TransactionRestApi {

    private final TransactionServiceApi transactionService;

    @Autowired
    public TransactionRest(TransactionServiceApi transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public String getTransaction( String authorizationHeader, TransactionRequest request) {
        return transactionService.getTransaction(request.getAccountID());
    }
}