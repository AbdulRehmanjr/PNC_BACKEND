package com.pnc.marketplace.service.implementation.stripe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.service.stripe.SubscriptionService;

@Service
public class SubscriptionSerivceImp implements SubscriptionService{

    @Value("${stripe.secert}")
    private String STRIPE_API;

    @Value("${stripe.product.stall}")
    private String stall;

    @Value("${stripe.product.bazaar}")
    private String bazaar;

    @Value("${stripe.product.elite}")
    private String elite;
    
    @Override
    public String paymentCheckout(String type, String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'paymentCheckout'");
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
    
}
