package com.adorsys.webank.exception;

public class InvalidJwtException extends BaseException {
    public InvalidJwtException(String message) {
        super("INVALID_JWT", message);
    }
} 