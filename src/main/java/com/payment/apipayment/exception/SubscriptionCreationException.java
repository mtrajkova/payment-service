package com.payment.apipayment.exception;

public class SubscriptionCreationException extends RuntimeException {
    public SubscriptionCreationException() {
        super("An error occurred while trying to create a subscription");
    }
}
