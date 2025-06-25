package com.adorsys.webank.exception;

public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super("BUSINESS_ERROR", message);
    }
} 