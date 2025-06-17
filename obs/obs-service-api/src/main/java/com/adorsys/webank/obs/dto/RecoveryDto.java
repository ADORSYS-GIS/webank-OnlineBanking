package com.adorsys.webank.obs.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
@Data
public class RecoveryDto {
    /**
     * The account ID with which the recovery is associated.
     */
    @NotBlank(message = "Account ID is required")
    private String accountId;
}
