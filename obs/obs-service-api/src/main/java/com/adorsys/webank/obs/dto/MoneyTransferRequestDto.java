package com.adorsys.webank.obs.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class MoneyTransferRequestDto {
    @NotBlank(message = "Recipient account ID is required")
    private String recipientAccountId;

    @NotBlank(message = "Sender account ID is required")
    private String senderAccountId;

    @NotBlank(message = "Amount is required")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Amount must be a valid number")
    private String amount;
}
