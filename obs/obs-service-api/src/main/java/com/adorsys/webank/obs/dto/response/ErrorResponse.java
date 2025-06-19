package com.adorsys.webank.obs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for API errors")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error code for specific error type", example = "INVALID_INPUT")
    private String errorCode;

    @Schema(description = "Error message describing what went wrong", example = "Invalid input parameters")
    private String message;

    @Schema(description = "Timestamp when the error occurred", example = "2024-03-20T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Path where the error occurred", example = "/api/v1/accounts/register")
    private String path;

    public enum ErrorCode {
        INVALID_INPUT,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        CONFLICT,
        INTERNAL_ERROR
    }
} 