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
@Schema(description = "Response object for money transfer operations")
public class MoneyTransferResponse {

    @Schema(description = "Status of the transfer operation", example = "COMPLETED")
    private TransferStatus status;

    @Schema(description = "The unique identifier of the transfer transaction", example = "TXN123456")
    private String transactionId;

    @Schema(description = "The amount transferred", example = "1000.00")
    private BigDecimal amount;

    @Schema(description = "The currency of the transfer", example = "USD")
    private String currency;

    @Schema(description = "Timestamp when the transfer was processed", example = "2024-03-20T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Response message with details about the transfer", example = "Transfer completed successfully")
    private String message;

    public enum TransferStatus {
        COMPLETED,          
        PENDING,            
        INSUFFICIENT_FUNDS,
        INVALID_ACCOUNT,   
        SYSTEM_ERROR 
    }
} 