package com.adorsys.webank.exception;

public class InvalidAmountException extends BaseException {
    public InvalidAmountException(String message) {
        super("INVALID_AMOUNT", message);
    }
} 