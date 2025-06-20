package com.adorsys.webank.obs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {
    private String status;
    private String message;
    private String balance;
    private String accountId;
    
    public static BalanceResponse success(String balance, String accountId) {
        return new BalanceResponse("SUCCESS", "Balance retrieved successfully", balance, accountId);
    }
    
    public static BalanceResponse serviceUnavailable(String accountId) {
        return new BalanceResponse("SERVICE_UNAVAILABLE", "External service is temporarily unavailable", "0", accountId);
    }
    
    public static BalanceResponse error(String message, String accountId) {
        return new BalanceResponse("ERROR", message, "0", accountId);
    }
} 