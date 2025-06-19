package com.adorsys.webank.obs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for withdrawal operations")
public class WithdrawalResponse {

    @Schema(description = "Status of the withdrawal operation", example = "COMPLETED")
    private WithdrawalStatus status;

    @Schema(description = "The unique identifier of the withdrawal transaction", example = "TXN123456")
    private String transactionId;

    @Schema(description = "The amount withdrawn", example = "200.00")
    private BigDecimal amount;

    @Schema(description = "The currency of the withdrawal", example = "USD")
    private String currency;

    @Schema(description = "Timestamp when the withdrawal was processed", example = "2024-03-20T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Response message with details about the withdrawal", example = "Withdrawal completed successfully")
    private String message;

    public enum WithdrawalStatus {
        COMPLETED,      
        PENDING,          
        INSUFFICIENT_FUNDS,
        SYSTEM_ERROR     
    }
} 