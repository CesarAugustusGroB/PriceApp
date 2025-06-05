package com.miempresa.priceapplication.exception;

public class InvalidCustomerRequestException extends RuntimeException {
    public InvalidCustomerRequestException(String message) {
        super(message);
    }
}
