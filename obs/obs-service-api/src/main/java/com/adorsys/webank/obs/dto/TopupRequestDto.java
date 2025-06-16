package com.adorsys.webank.obs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(
    name = "TopupRequest",
    description = "Request object for account top-up operations",
    example = """
        {
            "accountId": "ACC123456789",
            "amount": "1000.00",
            "currency": "EUR",
            "agentId": "AGT789",
            "reference": "TOPUP-2024-001",
            "description": "Monthly salary top-up"
        }
        """
)
public class TopupRequestDto {

    @Schema(
        description = "The account ID to be topped up",
        example = "ACC123456789",
        required = true,
        minLength = 10,
        maxLength = 20
    )
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "^[A-Z0-9]{10,20}$", message = "Account ID must be 10-20 alphanumeric characters")
    private String accountId;

    @Schema(
        description = "The amount to be added to the account",
        example = "1000.00",
        required = true,
        minimum = "0.01"
    )
    @NotBlank(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
}
