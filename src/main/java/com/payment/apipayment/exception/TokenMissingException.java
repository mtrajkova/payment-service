package com.payment.apipayment.exception;

public class TokenMissingException extends RuntimeException {
    public TokenMissingException() {
        super("Stripe token is missing. Please, try again later.");
    }
}
