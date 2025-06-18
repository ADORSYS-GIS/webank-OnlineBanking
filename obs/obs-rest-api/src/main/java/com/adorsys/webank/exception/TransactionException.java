package com.adorsys.webank.exception;

public class TransactionException extends BaseException {
    public TransactionException(String message) {
        super("TRANSACTION_ERROR", message);
    }
} 