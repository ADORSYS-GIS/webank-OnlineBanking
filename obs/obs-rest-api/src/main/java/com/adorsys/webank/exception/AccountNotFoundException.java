package com.adorsys.webank.exception;

public class AccountNotFoundException extends BaseException {
    public AccountNotFoundException(String message) {
        super("ACCOUNT_NOT_FOUND", message);
    }
} 