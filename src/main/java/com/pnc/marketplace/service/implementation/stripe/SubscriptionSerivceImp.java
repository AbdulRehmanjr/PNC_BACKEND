package com.pnc.marketplace.service.implementation.stripe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.database.stripe.SubscriptionRepository;
import com.pnc.marketplace.model.stripe.Subscription;
import com.pnc.marketplace.service.stripe.SubscriptionService;
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
public class SubscriptionSerivceImp implements SubscriptionService{

    @Value("${stripe.secert}")
    private String STRIPE_API;

  
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = STRIPE_API;
    }

    @Value("${web.domain}")
    private String Domain;


    @Autowired
    private StripeUtils stripUtils;

    @Autowired
    private SubscriptionRepository subscriptionRepo;


    @Override
    public String paymentCheckout(String type, String email) {

        Customer customer = this.stripUtils.checkAndCreateCustomer(email);
      
        if (customer == null)
            return null;

        ProductAllowed currentSubscription = this.stripUtils.getSubscriptionLevel(email);
        ProductAllowed selectedSubscription = null;
      
        //! To Stop User to subscribe from upper to lower tier
        //* Determine the selected subscription based on the current subscription level
        if (currentSubscription == null) {
            //* New subscription            
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
                    return null;

            }
        }

        //* Make the session for selected subscription
        if (selectedSubscription != null) {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl(Domain + "/home/become-seller")
                    .setCancelUrl(Domain + "/home/subscription-cards")
                    .setCustomer(customer.getId())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPrice(this.stripUtils.getPriceToken(selectedSubscription.name()))
                                    .build())
                    .build();

            Session session;
            try {
                session = Session.create(params);
                return session.getUrl();
            } catch (StripeException e) {
                log.info("ERROR: {}", e.getMessage());
                return null;
            }
        }
              
        return null;
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
            String SubscrptionId, String DiscountId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCustomer'");
    }

    @Override
    public com.pnc.marketplace.model.stripe.Subscription getCustomerByEmail(String email) {
        
        Subscription subscription = this.subscriptionRepo.findByEmail(email);

        if(subscription!=null)
            return subscription;
        
        log.info("No Customer Subscription found with given email: {}",email);
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
}
