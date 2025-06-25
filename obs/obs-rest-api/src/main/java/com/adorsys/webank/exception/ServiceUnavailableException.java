package com.adorsys.webank.exception;

public class ServiceUnavailableException extends BaseException {
    public ServiceUnavailableException(String message) {
        super("SERVICE_UNAVAILABLE", message);
    }
} 