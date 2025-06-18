package com.adorsys.webank.obs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "TopupResponse",
    description = "Response object for account top-up operations",
    example = """
        {
            "accountId": "ACC123456789",
            "amount": "1000.00",
            "currency": "EUR",
            "status": "COMPLETED",
            "transactionId": "TXN123456789",
            "message": "Top-up completed successfully"
        }
        """
)
public class TopupResponse {

    @Schema(
        description = "The account ID that was topped up",
        example = "ACC123456789"
    )
    private String accountId;

    @Schema(
        description = "The amount that was added to the account",
        example = "1000.00"
    )
    private BigDecimal amount;

    @Schema(
        description = "The currency of the top-up amount",
        example = "XAF"
    )
    private String currency;

    @Schema(
        description = "Status of the top-up operation",
        example = "COMPLETED"
    )
    private TopupStatus status;

    @Schema(
        description = "Unique identifier for the top-up transaction",
        example = "TXN123456789"
    )
    private String transactionId;

    @Schema(
        description = "Response message with details about the top-up",
        example = "Top-up completed successfully"
    )
    private String message;

    public enum TopupStatus {
        COMPLETED,           
        PENDING,           
        INVALID_AMOUNT,    
        FAILED            
    }
} 