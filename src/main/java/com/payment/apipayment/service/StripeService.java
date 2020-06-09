package com.payment.apipayment.service;

import com.payment.apipayment.exception.ChargeCreationException;
import com.payment.apipayment.exception.CustomerCreationException;
import com.payment.apipayment.exception.SubscriptionCreationException;
import com.payment.apipayment.exception.TokenMissingException;
import com.payment.apipayment.model.ChargeRequest;
import com.payment.apipayment.model.SubscriptionRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
    @Value("${stripe.keys.secret")
    private String API_SECRET_KEY;

    public StripeService() {
    }

    public String createCustomer(String email, String token) {
        String id = null;
        try {
            Stripe.apiKey = API_SECRET_KEY;
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("description", "Customer for " + email);
            customerParams.put("email", email);

            customerParams.put("source", token);

            Customer customer = Customer.create(customerParams);
            id = customer.getId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public String createSubscription(SubscriptionRequest subscriptionRequest, String token) {
        String id;

        if (token == null || subscriptionRequest.getPlan().isEmpty()) {
            throw new TokenMissingException();
        }

        Stripe.apiKey = API_SECRET_KEY;
        Map<String, Object> item = new HashMap<>();
        item.put("plan", subscriptionRequest.getPlan());

        String customerId = this.createCustomer(subscriptionRequest.getEmail(), token);
        if (customerId == null) {
            throw new CustomerCreationException();
        }

        Map<String, Object> items = new HashMap<>();
        items.put("0", customerId);

        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        params.put("items", items);

        if (!subscriptionRequest.getCoupon().isEmpty()) {
            params.put("coupon", subscriptionRequest.getCoupon());
        }

        try {
            Subscription subscription = Subscription.create(params);
            id = subscription.getId();
        } catch (StripeException e) {
            throw new SubscriptionCreationException();
        }
        return id;
    }

    public boolean cancelSubscription(String subscriptionId) {
        boolean status;
        try {
            Stripe.apiKey = API_SECRET_KEY;
            Subscription subscription = Subscription.retrieve(subscriptionId);
            subscription.cancel(null);
            status = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            status = false;
        }
        return status;
    }

    public Coupon retrieveCoupon(String code) {
        try {
            Stripe.apiKey = API_SECRET_KEY;
            return Coupon.retrieve(code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /*For charges, Stripe requires the amount to be in cents and not in dollars.
    If you are charging $20, make sure to pass 2000 to Stripe in the amount field. Also, this value must an integer*/
    public String createCharge(ChargeRequest chargeRequest, String token) {
        String id;

        if (token == null) {
            throw new TokenMissingException();
        }

        Stripe.apiKey = API_SECRET_KEY;
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", "usd");
        chargeParams.put("description", "Charge for " + chargeRequest.getEmail());
        chargeParams.put("source", token);

        try {
            Charge charge = Charge.create(chargeParams);
            id = charge.getId();
        } catch (StripeException ex) {
            throw new ChargeCreationException();
        }
        return id;
    }
}
