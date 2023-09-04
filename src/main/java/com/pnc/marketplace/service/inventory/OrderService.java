package com.pnc.marketplace.service.inventory;

import java.util.List;

import com.pnc.marketplace.model.Inventory.Order;

public interface OrderService {
    
    /**
     * The createOrder function creates and returns a new order.
     * 
     * @param order The order parameter is an object of type Order, which represents an order that
     * needs to be created.
     * @return The method createOrder is returning an object of type Order.
     */
    Order createOrder(Order order);

    /**
     * The function getOrderById retrieves an order object based on its unique identifier.
     * 
     * @param orderId A unique identifier for the order.
     * @return The method getOrderById returns an Order object.
     */
    Order getOrderById(long orderId);

    
    /**
     * The function retrieves all orders associated with a given user email.
     * 
     * @param email The email parameter is a string that represents the email address of a user.
     * @return The method getAllOrderByUserEmail returns a list of Order objects.
     */
    List<Order> getAllOrderByUserEmail(String email);

    /**
     * The function "getOrderByCode" takes an order code as input and returns the corresponding Order
     * object.
     * 
     * @param orderCode A string representing the code of the order.
     * @return The method getOrderByCode returns an order object.
     */
    Order getOrderByCode(String orderCode);

    
    /**
     * The function "getOrdersBySeller" returns a list of orders associated with a specific seller.
     * 
     * @param sellerId The sellerId parameter is an integer that represents the unique identifier of a
     * seller.
     * @return The method is returning a list of orders.
     */
    List<Order> getOrdersBySeller(int sellerId);
}
