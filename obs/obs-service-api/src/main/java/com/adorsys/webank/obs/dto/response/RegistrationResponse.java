package com.adorsys.webank.obs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "RegistrationResponse",
    description = "Response object for account registration operations",
    example = """
        {
            "accountId": "ACC123456789",
            "status": "SUCCESS",
            "message": "Account registered successfully. Your account is now active."
        }
        """
)
public class RegistrationResponse {

    @Schema(
        description = "The unique identifier of the registered account",
        example = "ACC123456789"
    )
    private String accountId;

    @Schema
    (
        description = "The certificate associated with the account, used for secure transactions",
        example = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQ..."
    )
    private String accountCertificate;

    @Schema(
        description = "Status of the registration process",
        example = "SUCCESS"
    )
    private RegistrationStatus status;

    @Schema(
        description = "Response message with details about the registration",
        example = "Account registered successfully. Your account is now active."
    )
    private String message;

    public enum RegistrationStatus {
        SUCCESS,    
        FAILED     
    }
} 