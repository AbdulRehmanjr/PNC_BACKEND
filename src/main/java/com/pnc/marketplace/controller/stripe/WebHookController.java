package com.pnc.marketplace.controller.stripe;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pnc.marketplace.service.stripe.SubscriptionService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account.Settings.Dashboard;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/webhook")
public class WebHookController {

    
    
    @Value("${stripe.secert}")
    private String STRIPEAPI;

    @PostConstruct
    public void init() {
        Stripe.apiKey = STRIPEAPI;
    }

    @Value("${stripe.webhook}")
    private String endpointSecret;

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/events")
    public ResponseEntity<?> handleWebhookEvent(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        
        try {

            Event event = Webhook.constructEvent(payload, signature, endpointSecret);
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;

            if (dataObjectDeserializer.getObject().isPresent()) {

                stripeObject = dataObjectDeserializer.getObject().get();

                switch (event.getType()) {
                    // ! may be handle later
                    case "customer.subscription.created":
                     Subscription newSubscription = (Subscription) event.getDataObjectDeserializer().getObject()
                                .orElse(null);
                    String subscriptionId = newSubscription.getId();
                    String customerId = newSubscription.getCustomer();
                    String email = "";

                    try {
                        Customer customer = Customer.retrieve(customerId);
                        Subscription subscription = Subscription.retrieve(subscriptionId);

                        Price price = Price
                                .retrieve(subscription.getItems().getData().get(0).getPrice().getId());
                        Product product = Product.retrieve(price.getProduct());

                        email = customer.getEmail();

                        log.info("Product Name ==> {}",product.getName());
                        // this.sellerService.addSubscription(card, email, price.getUnitAmount());

                        // this.subService.addCustomer(email, customerId, subscriptionId, discountId);

                    } catch (StripeException e) {
                        log.error("ERROR: {} MESSAGE: {}", e.getCause(), e.getMessage());
                    } catch (Exception e) {
                        log.error("ERROR: {}", e.getMessage());
                    }
                    break;
                case "customer.subscription.updated":
                     Subscription updatedSubscription = (Subscription) event.getDataObjectDeserializer().getObject()
                                .orElse(null);
                    
                    if(updatedSubscription!=null){
                        
                        
                        String updatedCustomerId = updatedSubscription.getCustomer();
                        String updatedCustomerEmail = "";

                        try {
                            Customer customer = Customer.retrieve(updatedCustomerId);
                            updatedCustomerEmail = customer.getEmail();

                            com.pnc.marketplace.model.stripe.Subscription existingSubscription = this.subscriptionService
                                    .getCustomerByEmail(updatedCustomerEmail);

                            if (existingSubscription != null) {

                                existingSubscription.setDateValid(LocalDate.now().plusDays(30));

                                this.subscriptionService.updateSubscription(existingSubscription);
                            }
                        } catch (StripeException e) {
                            log.error("ERROR: {} MESSAGE: {}", e.getCause(), e.getMessage());
                        } catch (Exception e) {
                            log.error("ERROR: {}", e.getMessage());
                        }
                    }
                    break;
                case "customer.subscription.deleted":
                    // Handle subscription cancellation or end
                    break;
                case "payment_intent.succeeded":
                    // Handle successful payment intent
                    break;
                case "payment_intent.payment_failed":
                    // Handle failed payment intent
                    break;
                case "checkout.session.completed":
                    // Handle checkout session completion
                    break;
                default:    
                    log.info("Case event not implemented");

                }
            } else {
                log.error("Error while making the event object serialization");
            }
            return ResponseEntity.status(201).build();
        } catch (SignatureVerificationException e) {
            log.error("Error : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
