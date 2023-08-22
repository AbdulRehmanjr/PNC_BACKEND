package com.pnc.marketplace.database.stripe;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.stripe.Subscription;

public interface SubscriptionRepository  extends JpaRepository<Subscription,Integer>{
    
    /**
     * The function findByEmail takes an email as input and returns a Subscription object.
     * 
     * @param email The email parameter is a string that represents the email address of a user.
     * @return The method findByEmail is returning a Subscription object.
     */
    Subscription findByEmail(String email);
}
