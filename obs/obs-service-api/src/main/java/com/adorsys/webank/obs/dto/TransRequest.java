package com.adorsys.webank.obs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "TransactionRequest",
    description = "Request object for retrieving account transactions",
    example = """
        {
            "accountID": "ACC123456789",
            "fromDate": "2024-01-01",
            "toDate": "2024-03-20",
        """
)
public class TransRequest {

    @Schema(
        description = "The account ID to retrieve transactions for",
        example = "ACC123456789",
        required = true,
        minLength = 10,
        maxLength = 20
    )
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "^[A-Z0-9]{10,20}$", message = "Account ID must be 10-20 alphanumeric characters")
    private String accountID;

}
