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
public class RecoveryDto {

    @Schema(
        description = "The account ID to recover",
        example = "ACC123456789",
        required = true,
        minLength = 10,
        maxLength = 20
    )
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Amount must be a valid number")
    private String accountId;
}
