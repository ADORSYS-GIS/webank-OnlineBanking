package com.adorsys.webank.obs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "MoneyTransferRequest",
    description = "Request object for money transfer operations",
    example = """
        {
            "recipientAccountId": "ACC123456789",
            "senderAccountId": "ACC987654321",
            "amount": "500.00",
            "currency": "EUR",
            "reference": "TRANS-2024-001",
            "description": "Monthly rent payment"
        }
        """
)
public class MoneyTransferRequestDto {

    @Schema(
        description = "The recipient account ID for the transfer",
        example = "ACC123456789",
        required = true,
        minLength = 10,
        maxLength = 20
    )
    @NotBlank(message = "Recipient account ID is required")
    @Pattern(regexp = "^[A-Z0-9]{10,20}$", message = "Recipient account ID must be 10-20 alphanumeric characters")
    private String recipientAccountId;

    @Schema(
        description = "The sender account ID for the transfer",
        example = "ACC987654321",
        required = true,
        minLength = 10,
        maxLength = 20
    )
    @NotBlank(message = "Sender account ID is required")
    @Pattern(regexp = "^[A-Z0-9]{10,20}$", message = "Sender account ID must be 10-20 alphanumeric characters")
    private String senderAccountId;

    @Schema(
        description = "The amount to be transferred",
        example = "500.00",
        required = true,
        minimum = "0.01"
    )
    @NotBlank(message = "Amount is required")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Amount must be a valid number")
    private BigDecimal amount;
}
