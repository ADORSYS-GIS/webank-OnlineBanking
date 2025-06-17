package com.adorsys.webank.exception;

public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final String message;

    protected BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
} 