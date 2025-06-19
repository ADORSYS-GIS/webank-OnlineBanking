package com.adorsys.webank.obs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "TransactionHistoryResponse",
    description = "Response object for account transaction history",
    example = """
        {
            "status": "SUCCESS",
            "message": "Transaction history retrieved successfully",
            "timestamp": "2024-03-20T10:30:00",
            "data": "ACC123456789"
        }
        """
)
public class TransactionHistoryResponse {

    @Schema(
        description = "Status of the transaction history retrieval",
        example = "SUCCESS"
    )
    private TransactionStatus status;

    @Schema(
        description = "Response message with details about the operation",
        example = "Transaction history retrieved successfully"
    )
    private String message;

    @Schema(
        description = "Timestamp when the response was generated",
        example = "2024-03-20T10:30:00"
    )
    private LocalDateTime timestamp;

    @Schema(
        description = "The account ID for which transactions were retrieved",
        example = "ACC123456789"
    )
    private String data;

    public enum TransactionStatus {
        SUCCESS,
        PENDING,
        FAILED,
      
    }
}
