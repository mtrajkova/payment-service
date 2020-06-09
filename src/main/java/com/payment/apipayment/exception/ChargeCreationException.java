package com.payment.apipayment.exception;

public class ChargeCreationException extends RuntimeException {
    public ChargeCreationException() {
        super("An error occurred while trying to create a charge");
    }
}
