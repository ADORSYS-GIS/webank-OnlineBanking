package com.adorsys.webank.exception;

public class InsufficientBalanceException extends BaseException {
    public InsufficientBalanceException(String message) {
        super("INSUFFICIENT_BALANCE", message);
    }
} 