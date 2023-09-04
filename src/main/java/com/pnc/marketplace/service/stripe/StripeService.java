package com.pnc.marketplace.service.stripe;

import com.pnc.marketplace.model.Inventory.Cart;
import com.pnc.marketplace.model.seller.Seller;
import com.pnc.marketplace.model.stripe.Subscription;
import com.stripe.model.Customer;

public interface StripeService {

    /**
     * The paymentCheckout function takes in a type and email as parameters and
     * returns a string.
     * 
     * @param type  The type of payment method the user wants to use for checkout.
     *              This can be "credit
     *              card", "debit card", "PayPal", or any other supported payment
     *              method.
     * @param email The email parameter is a string that represents the email
     *              address of the user
     *              making the payment.
     * @return The method is returning a string value.
     */
    String paymentCheckout(String type, String email);

    /**
     * The function orderPaymentCheckout takes a Cart object as input and returns a
     * String representing
     * the payment checkout for the order.
     * 
     * @param cart The cart parameter is an object of type Cart, which represents
     *             the shopping cart
     *             containing the items that the user wants to purchase.
     * @return The method "orderPaymentCheckout" is returning a String.
     */
    String orderPaymentCheckout(Cart cart);

    /**
     * The addSubscription function adds a subscription for a product to a seller's
     * account using the
     * provided email and amount.
     * 
     * @param productName The name of the product for which the subscription is
     *                    being added.
     * @param email       The email parameter is a string that represents the email
     *                    address of the customer
     *                    who wants to subscribe to the product.
     * @param amount      The amount parameter represents the cost or price of the
     *                    subscription.
     * @return The method addSubscription is returning a Subscription object.
     */
    Seller addSubscription(String productName, String email, long amount);

    /**
     * The function "updateSubscription" takes an email as input and returns a
     * string.
     * 
     * @param email A string representing the email address of the user.
     * @return The method is returning a String value.
     */
    String updateSubscription(String email);

    /**
     * The function "getSubscriptionId" takes an email as input and returns the
     * subscription ID
     * associated with that email.
     * 
     * @param email A string representing the email address of a user.
     * @return The method is returning a string, which is the subscription ID
     *         associated with the given
     *         email.
     */
    String getSubscriptionId(String email);

    /**
     * The function addCustomer takes in email, customerId, SubscriptionId, and
     * DiscountId as
     * parameters and returns a Subscription object.
     * 
     * @param email         The email parameter is a string that represents the
     *                      email address of the customer.
     *                      This is used to identify and communicate with the
     *                      customer.
     * @param customerId    The unique identifier for the customer.
     * @param SubscrptionId The ID of the subscription that the customer wants to
     *                      add. This could be a
     *                      unique identifier for a specific subscription plan or
     *                      package.
     * @return The addCustomer method is returning a Subscription object.
     */
    Subscription addCustomer(String email, String customerId, String SubscrptionId);

    /**
     * The function "getCustomerByEmail" retrieves a subscription object based on
     * the customer's email.
     * 
     * @param email A string representing the email address of the customer.
     * @return The method is returning a Subscription object.
     */
    Subscription getCustomerByEmail(String email);

    /**
     * The deleteSubscription function deletes a subscription associated with a
     * given email.
     * 
     * @param email The email parameter is a string that represents the email
     *              address of the
     *              subscription that needs to be deleted.
     */
    void deleteSubscription(String email);

    /**
     * The function "updateSubscription" updates a subscription and returns the
     * updated subscription.
     * 
     * @param subscription The subscription object that needs to be updated.
     * @return The method is returning a Subscription object.
     */
    Subscription updateSubscription(Subscription subscription);

    /**
     * The function retrieves a customer object based on a given Stripe ID.
     * 
     * @param stripeId The stripeId parameter is a unique identifier for a customer
     *                 in the Stripe
     *                 payment system.
     * @return The method retrieveCustomer is returning a Customer object.
     */
    Customer retrieveCustomer(String stripeId);

}