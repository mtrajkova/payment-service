package com.payment.apipayment.exception;

public class CustomerCreationException extends RuntimeException {
    public CustomerCreationException() {
        super("An error occurred while trying to create a customer");
    }
}
