package com.payment.apipayment.model;

public class ChargeRequest {
    private String email;
    private String amount;

    public ChargeRequest() {
    }

    public ChargeRequest(String email, String amount) {
        this.email = email;
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public String getAmount() {
        return amount;
    }
}
