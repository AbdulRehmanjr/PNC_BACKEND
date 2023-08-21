package com.pnc.marketplace.service.stripe;

public interface SubscriptionService {
    
    
    /**
     * The paymentCheckout function takes in a type and email as parameters and returns a string.
     * 
     * @param type The type of payment method the user wants to use for checkout. This can be "credit
     * card", "debit card", "PayPal", or any other supported payment method.
     * @param email The email parameter is a string that represents the email address of the user
     * making the payment.
     * @return The method is returning a string value.
     */
    String paymentCheckout(String type,String email);

    //String projectPayment(Order order);

    /**
     * The function "updateSubscription" takes an email as input and returns a string.
     * 
     * @param email A string representing the email address of the user.
     * @return The method is returning a String value.
     */
    String updateSubscription(String email);
    
    /**
     * The function "getSubscriptionId" takes an email as input and returns the subscription ID
     * associated with that email.
     * 
     * @param email A string representing the email address of a user.
     * @return The method is returning a string, which is the subscription ID associated with the given
     * email.
     */
    String getSubscriptionId(String email);
}
