package com.adorsys.webank.obs.dto;

import lombok.Data;

@Data
public class MoneyTransferRequestDto {
    private String recipientAccountId;
    private String senderAccountId;
    private String amount;
}
