package com.miempresa.priceapplication.exception;

public class InvalidPriceRequestException extends RuntimeException {
    public InvalidPriceRequestException(String message) {
        super(message);
    }
}
