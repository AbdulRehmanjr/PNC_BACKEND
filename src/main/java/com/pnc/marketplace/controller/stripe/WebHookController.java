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

import com.pnc.marketplace.model.Inventory.Cart;
import com.pnc.marketplace.model.Inventory.Order;
import com.pnc.marketplace.service.inventory.CartService;
import com.pnc.marketplace.service.inventory.OrderService;
import com.pnc.marketplace.service.stripe.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
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
    private StripeService stripeService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

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

                            this.stripeService.addSubscription(product.getName(), email, price.getUnitAmount());

                            this.stripeService.addCustomer(email, customerId, subscriptionId);

                        } catch (StripeException e) {
                            log.error("ERROR: {} MESSAGE: {}", e.getCause(), e.getMessage());
                        } catch (Exception e) {
                            log.error("ERROR: {}", e.getMessage());
                        }
                        break;
                    case "customer.subscription.updated":
                        Subscription updatedSubscription = (Subscription) event.getDataObjectDeserializer().getObject()
                                .orElse(null);

                        if (updatedSubscription != null) {

                            String updatedCustomerId = updatedSubscription.getCustomer();
                            String updatedCustomerEmail = "";

                            try {
                                Customer customer = Customer.retrieve(updatedCustomerId);
                                updatedCustomerEmail = customer.getEmail();

                                com.pnc.marketplace.model.stripe.Subscription existingSubscription = this.stripeService
                                        .getCustomerByEmail(updatedCustomerEmail);

                                if (existingSubscription != null) {

                                    existingSubscription.setDateValid(LocalDate.now().plusDays(30));

                                    this.stripeService.updateSubscription(existingSubscription);
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

                        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

                        Map<String, String> metadata = paymentIntent.getMetadata();
                        long cartId = Integer.parseInt(metadata.get("cartId"));
                        
                        // update the cart as paid 
                        Cart cart = this.cartService.updateCart(this.cartService.fetchCartById(cartId));

                        /**
                         * * Make orde object and set it as order done
                         */
                        Order order = new Order();

                        order.setCart(cart);
                        order.setCustomerEmail(cart.getUserEmail());
                        order.setAmount(paymentIntent.getAmount()/100);
                        
                        this.orderService.createOrder(order);

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
