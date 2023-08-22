package com.pnc.marketplace.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.database.stripe.SubscriptionRepository;
import com.pnc.marketplace.model.seller.Seller;
import com.pnc.marketplace.model.stripe.Subscription;
import com.pnc.marketplace.service.seller.SellerService;
import com.pnc.marketplace.utils.enums.ProductAllowed;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class StripeUtils {

    @Value("${stripe.product.stall}")
    private String stall;

    @Value("${stripe.product.bazaar}")
    private String bazaar;

    @Value("${stripe.product.elite}")
    private String elite;
    
    @Autowired
    private  SubscriptionRepository subscriptionRepo;

    @Autowired
    private SellerService sellerService;
    

    /**
     * The function `checkAndCreateCustomer` checks if a customer with the given email exists in the
     * subscription repository, and if not, creates a new customer using the Stripe API.
     * 
     * @param email The email parameter is a string that represents the email address of the customer.
     * @return The method `checkAndCreateCustomer` returns a `Customer` object.
     */
    public Customer checkAndCreateCustomer(String email) {

        Subscription subscription = this.subscriptionRepo.findByEmail(email);

        if (subscription == null) {
            try {
                CustomerCreateParams params = CustomerCreateParams.builder()
                        .setEmail(email)
                        .build();
                Customer customer = Customer.create(params);

                return customer;
            } catch (StripeException e) {
                log.error("Error : {}", e.getMessage());
                return null;
            }
        }
        try {
            
            Customer customer = Customer.retrieve(subscription.getCustomerId());
            return customer;
        } catch (StripeException e) {
            log.error("Error : {}", e.getMessage());
            return null;
        }

    }

    public ProductAllowed getSubscriptionLevel(String email) {

        Seller seller = this.sellerService.getSellerByEmail(email);

        if (seller != null) {
            String type = seller.getSellerType();
            if (type.equals("NONE")) {
                return null;
            }
            try {
                return ProductAllowed.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("MESSAGE : {}", e.getMessage());
            }
        }
        return null;
    }

   /**
    * The getPriceToken function returns a specific token based on the type parameter.
    * 
    * @param type The "type" parameter is a string that represents the type of token. It can have three
    * possible values: "DEWDROPPER", "SPRINKLE", or any other value.
    * @return The method is returning a String value. The specific value being returned depends on the
    * value of the "type" parameter. If "type" is equal to "DEWDROPPER", the method will return the
    * value of the "stall" variable. If "type" is equal to "SPRINKLE", the method will return the value
    * of the "bazaar" variable. Otherwise,
    */
    public String getPriceToken(String type) {

        switch (type.toUpperCase()) {
            case "STARTERSTALL":
                return this.stall;
            case "LOCALBAZAAR":
                return this.bazaar;
            default:
                return this.elite;
        }
        
    }
}
