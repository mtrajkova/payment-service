package com.payment.apipayment.api;

import com.payment.apipayment.model.ChargeRequest;
import com.payment.apipayment.model.SubscriptionRequest;
import com.payment.apipayment.service.StripeService;
import com.stripe.model.Coupon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @Value("${stripe.keys.public}")
    private String API_PUBLIC_KEY;

    private StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> createSubscription(@RequestHeader("token") String token, @RequestBody SubscriptionRequest subscriptionRequest) {
        String subscriptionId = stripeService.createSubscription(subscriptionRequest, token);

        // Ideally you should store customerId and subscriptionId along with customer object here.
        // These values are required to update or cancel the subscription at later stage.
        return ResponseEntity.ok("Success! Your subscription id is " + subscriptionId);
    }

    @PostMapping("/cancel-subscription")
    public ResponseEntity<String> cancelSubscription(String subscriptionId) {
        boolean status = stripeService.cancelSubscription(subscriptionId);
        if (!status) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel subscription. Please, try again later.");
        }
        return ResponseEntity.ok("Subscription successfully cancelled.");
    }

    @PostMapping("/coupon-validatior")
    public ResponseEntity<String> couponValidator(String code) {
        Coupon coupon = stripeService.retrieveCoupon(code);
        if (coupon != null && coupon.getValid()) {
            String details = (coupon.getPercentOff() == null ? "$" + (coupon.getAmountOff()) / 100 : coupon.getPercentOff() + "%") +
                    "OFF" + coupon.getDuration();
            return ResponseEntity.ok(details);
        } else {
            return
                    ResponseEntity.status(HttpStatus.CONFLICT).body("This coupon code is not available. " +
                            "This may be because it has expired or has already been applied to your account.");
        }
    }

    @PostMapping("/create-charge")
    public ResponseEntity<String> createCharge(@RequestHeader("token") String token, @RequestBody ChargeRequest chargeRequest) {
        String chargeId = stripeService.createCharge(chargeRequest, token);
        return ResponseEntity.ok("Success! Your chargeId is " + chargeId);
    }
}
