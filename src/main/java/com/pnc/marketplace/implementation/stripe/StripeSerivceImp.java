package com.pnc.marketplace.implementation.stripe;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.database.seller.SellerDashboardRepository;
import com.pnc.marketplace.database.seller.SellerRepository;
import com.pnc.marketplace.database.stripe.SubscriptionRepository;
import com.pnc.marketplace.model.User;
import com.pnc.marketplace.model.Inventory.Cart;
import com.pnc.marketplace.model.seller.Seller;
import com.pnc.marketplace.model.seller.SellerDashboard;
import com.pnc.marketplace.model.stripe.Subscription;
import com.pnc.marketplace.service.inventory.CartService;
import com.pnc.marketplace.service.stripe.StripeService;
import com.pnc.marketplace.service.user.UserService;
import com.pnc.marketplace.utils.StripeUtils;
import com.pnc.marketplace.utils.enums.ProductAllowed;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StripeSerivceImp implements StripeService {

    @Value("${stripe.secert}")
    private String STRIPE_API;

    @PostConstruct
    public void init() {
        Stripe.apiKey = STRIPE_API;
    }

    @Value("${web.domain}")
    private String Domain;

    @Autowired
    private StripeUtils stripeUtils;

    @Autowired
    private SubscriptionRepository subscriptionRepo;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerDashboardRepository sdRepo;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Override
    public String paymentCheckout(String type, String email) {

        Customer customer = this.stripeUtils.checkAndCreateCustomer(email);

        if (customer == null)
            return null;

        ProductAllowed currentSubscription = this.stripeUtils.getSubscriptionLevel(email);
        ProductAllowed selectedSubscription = null;

        // ! To Stop User to subscribe from upper to lower tier
        // * Determine the selected subscription based on the current subscription level
        if (currentSubscription == null) {
            // * New subscription
            selectedSubscription = ProductAllowed.valueOf(type.toUpperCase());
        } else {
            switch (currentSubscription) {
                case STARTERSTALL:
                    if ("STARTERSTALL".equals(type) || "LOCALBAZAAR".equals(type))
                        selectedSubscription = ProductAllowed.valueOf(type.toUpperCase());
                    break;
                case LOCALBAZAAR:
                    if ("EMPORIUMELITE".equals(type))
                        selectedSubscription = ProductAllowed.valueOf(type.toUpperCase());
                    break;
                case EMPORIUMELITE:
                    selectedSubscription = ProductAllowed.valueOf(type.toUpperCase());
                    break;
                default:
                    return null;
            }
        }

        // * Make the session for selected subscription
        if (selectedSubscription != null) {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl(Domain + "/home/become-seller")
                    .setCancelUrl(Domain + "/home/subscription-cards")
                    .setCustomer(customer.getId())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPrice(this.stripeUtils.getPriceToken(selectedSubscription.name()))
                                    .build())
                    .build();

            Session session;
            try {
                session = Session.create(params);
                log.info("Session : {}", session);
                return session.getUrl();
            } catch (StripeException e) {
                log.info("ERROR: {}", e.getMessage());
                return null;
            }
        }

        return null;
    }

    /**
     * The `orderPaymentCheckout` function calculates the total amount of a cart,
     * creates a Stripe
     * session for payment, and returns the session URL.
     * 
     * @param cart The `cart` parameter is an object of the `Cart` class, which
     *             represents the shopping
     *             cart containing the items to be ordered and checked out.
     * @return The method is returning a String, which is the URL of the session for
     *         the payment
     *         checkout.
     */
    @Override
    public String orderPaymentCheckout(Cart cart) {

        long shipingFee = 300L;
        AtomicLong amount = new AtomicLong(0L);

        cart.getCartItems().forEach(
                item -> {
                    amount.addAndGet((long) (item.getProductQuantity() * item.getProductPrice()));
                });

        Cart response = this.cartService.createCart(cart);


        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(Domain + "/home/cart-details")
                .setCancelUrl(Domain + "/home/cart-details")
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putMetadata("cartId", String.valueOf(response.getCartId())).build())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("PKR")
                                                .setUnitAmount((amount.get() + shipingFee) * 100L)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(cart.getUserEmail())
                                                                .build())
                                                .build())
                                .build())
                .build();

        Session session;
        try {
            session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            log.info("ERROR creating Session: {}", e.getMessage());
            return null;
        }

    }

    @Override
    public String updateSubscription(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSubscription'");
    }

    @Override
    public String getSubscriptionId(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSubscriptionId'");
    }

    @Override
    public com.pnc.marketplace.model.stripe.Subscription addCustomer(String email, String customerId,
            String SubscrptionId) {

        log.info("Saving a new Subscription in the database");

        Subscription existingSubscription = this.subscriptionRepo.findByEmail(email);

        if (existingSubscription != null) {

            existingSubscription.setCustomerId(customerId);
            existingSubscription.setSubscriptionId(SubscrptionId);
            existingSubscription.setDateValid(LocalDate.now().plusDays(30));

            try {
                return this.subscriptionRepo.save(existingSubscription);
            } catch (Exception e) {
                log.error("Error occurred while updating the subscription: {}", e.getMessage());
            }

        } else {
            // * Create a new subscription entry
            log.info("Saving new Subscription");

            Subscription newSubscription = new Subscription();

            newSubscription.setEmail(email);
            newSubscription.setCustomerId(customerId);
            newSubscription.setSubscriptionId(SubscrptionId);
            newSubscription.setDateValid(LocalDate.now().plusDays(30));

            try {
                Subscription savedSubscription = this.subscriptionRepo.save(newSubscription);
                return savedSubscription;
            } catch (Exception e) {
                log.error("Error occurred while saving the subscription: {}", e.getMessage());
            }
        }

        return null;
    }

    @Override
    public com.pnc.marketplace.model.stripe.Subscription getCustomerByEmail(String email) {

        Subscription subscription = this.subscriptionRepo.findByEmail(email);

        if (subscription != null)
            return subscription;

        log.info("No Customer Subscription found with given email: {}", email);
        return null;
    }

    @Override
    public void deleteSubscription(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSubscription'");
    }

    @Override
    public com.pnc.marketplace.model.stripe.Subscription updateSubscription(
            com.pnc.marketplace.model.stripe.Subscription subscription) {

        log.info("Updating the subscription data");

        return this.subscriptionRepo.save(subscription);
    }

    @Override
    public Customer retrieveCustomer(String stripeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retrieveCustomer'");
    }

    /**
     * The addSubscription function updates the seller's subscription and dashboard
     * based on the
     * product name and returns the updated seller object.
     * 
     * @param productName The productName parameter is a String that represents the
     *                    name of the product
     *                    for which the subscription is being added.
     * @param email       The email parameter is a String that represents the email
     *                    of the seller.
     * @param amount      The amount parameter represents the subscription amount
     *                    for the seller.
     * @return The method is currently returning null.
     */
    @Override
    public Seller addSubscription(String productName, String email, long amount) {

        Seller response = this.sellerRepository.findByEmail(email);

        SellerDashboard sellerDashboard = this.sdRepo.findByEmail(email);

        if (sellerDashboard == null)
            sellerDashboard = new SellerDashboard();

        sellerDashboard.setUserId(response.getSellerId());
        sellerDashboard.setEmail(response.getEmail());
        sellerDashboard.setSellerType(response.getSellerType());

        if (productName.equals("STALL")) {

            sellerDashboard.setJobs(ProductAllowed.valueOf("STARTERSTALL").getValue());
            response.setSellerType("STARTERSTALL");
            response.setMaxProducts(ProductAllowed.valueOf("STARTERSTALL").getValue());
        } else if (productName.equals("BAZAAR")) {

            sellerDashboard.setJobs(ProductAllowed.valueOf("LOCALBAZAAR").getValue());
            response.setSellerType("LOCALBAZAAR");
            response.setMaxProducts(ProductAllowed.valueOf("LOCALBAZAAR").getValue());
        } else if (productName.equals("ELITE")) {

            sellerDashboard.setJobs(ProductAllowed.valueOf("EMPORIUMELITE").getValue());
            response.setSellerType("EMPORIUMELITE");
            response.setMaxProducts(ProductAllowed.valueOf("EMPORIUMELITE").getValue());
        }

        // * Seller , Dashboard modification and adding user credentials

        response.setIsActive(true);
        this.sellerRepository.save(response);
        this.sdRepo.save(sellerDashboard);

        User user = new User();

        user.setFirstName(response.getFirstName());
        user.setLastName(response.getLastName());
        user.setPhotoUri(response.getPicture());
        user.setUserEmail(response.getEmail());
        user.setUserPassword(response.getFirstName() + response.getLastName());
        this.userService.saveSeller(user);

        return response;
    }

}
