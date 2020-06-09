package com.payment.apipayment.model;

public class SubscriptionRequest {
    private String email;
    private String coupon;
    private String plan;

    public SubscriptionRequest() {
    }

    public SubscriptionRequest(String email, String coupon, String plan) {
        this.email = email;
        this.coupon = coupon;
        this.plan = plan;
    }

    public String getEmail() {
        return email;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getPlan() {
        return plan;
    }
}
