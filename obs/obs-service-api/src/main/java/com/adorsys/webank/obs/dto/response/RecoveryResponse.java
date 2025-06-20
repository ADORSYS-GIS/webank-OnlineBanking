package com.adorsys.webank.obs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    name = "RecoveryResponse",
    description = "Response object for account recovery operations",
    example = """
        {
            "accountId": "ACC123456789",
            "status": "INITIATED",
            "message": "Recovery process has been initiated. Please check your email for verification steps."
        }
        """
)
public class RecoveryResponse {
    
    @Schema(
        description = "The account ID that was requested for recovery",
        example = "ACC123456789"
    )
    private String accountId;
    
    @Schema(
        description = "Current status of the recovery process",
        example = "INITIATED"
    )
    private RecoveryStatus status;
    
    @Schema(
        description = "Human-readable message about the recovery status",
        example = "Recovery process has been initiated. Please check your email for verification steps."
    )
    private String message;
    
    public enum RecoveryStatus {
        INITIATED,    
        PENDING,     
        COMPLETED,  
        FAILED    
    }
} 