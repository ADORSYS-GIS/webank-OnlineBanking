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
@Schema(description = "Response object for account balance operations")
public class BalanceResponse {

    @Schema(description = "Status of the balance retrieval", example = "AVAILABLE")
    private BalanceStatus status;

    @Schema(description = "The account identifier", example = "ACC123456789")
    private String accountId;

    @Schema(description = "Timestamp when the balance was retrieved", example = "2024-03-20T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Response message with details about the balance", example = "Balance retrieved successfully")
    private String message;

    @Schema(description = "The current balance amount", example = "1000.00")
    private BigDecimal balance;

    public enum BalanceStatus {
        AVAILABLE,
        INSUFFICIENT_FUNDS,
        SYSTEM_ERROR
    }
} 