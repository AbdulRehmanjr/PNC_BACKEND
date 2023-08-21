package com.pnc.marketplace.controller.stripe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pnc.marketplace.model.stripe.SubscriptionPayment;
import com.pnc.marketplace.service.stripe.SubscriptionService;
import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Value("${stripe.secert}")
    private String STRIPEAPI;

    @PostConstruct
    public void init() {
        Stripe.apiKey = STRIPEAPI;
    }

    @Value("${stripe.webhook}")
    private String endpointSecret;

   /**
    * The function creates a checkout session for a subscription payment and returns the URL for the
    * payment.
    * 
    * @param payment The `payment` parameter is of type `SubscriptionPayment`, which is a custom class
    * representing the payment details for a subscription. It contains the following properties:
    * @return The method is returning a ResponseEntity object. If the url is not null, it returns a
    * ResponseEntity with a status of 201 (Created) and the url as the body. If the url is null, it
    * returns a ResponseEntity with a status of 404 (Not Found) and a null body.
    */
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody SubscriptionPayment payment) {

        String url = this.subscriptionService.paymentCheckout(payment.getType(), payment.getEmail());

        if (url != null) 
            return ResponseEntity.status(201).body(url);

        log.error("Error in making stripe url");
        return ResponseEntity.status(404).body(null);
    }
}
