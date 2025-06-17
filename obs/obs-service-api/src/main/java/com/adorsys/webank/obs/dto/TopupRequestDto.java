package com.adorsys.webank.obs.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
@Data
public class TopupRequestDto {


    @NotBlank(message = "Account ID is required")
    private String accountId;

    @NotBlank(message = "Amount is required")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Amount must be a valid number")
    private String amount;
}
