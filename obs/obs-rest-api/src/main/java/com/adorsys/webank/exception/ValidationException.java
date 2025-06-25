package com.adorsys.webank.exception;

public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
    }
} 