package com.adorsys.webank.obs.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class BalanceRequest {
    /**
     * The account ID for which the balance is requested.
     */
    @NotBlank(message = "Account ID is required")
    private String accountID;
}
