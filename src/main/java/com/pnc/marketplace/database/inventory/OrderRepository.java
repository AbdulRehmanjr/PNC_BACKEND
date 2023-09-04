package com.pnc.marketplace.database.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.Inventory.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{
    
    /**
     * The function `findAllByUserEmail` returns a list of orders associated with a specific user
     * email.
     * 
     * @param email The email parameter is a string that represents the email address of a user.
     * @return The method is returning a list of orders that are associated with the specified user
     * email.
     */
    List<Order> findAllByCustomerEmail(String email);

    /**
     * The function findByOrderCode returns an Order object based on the given order code.
     * 
     * @param code The code parameter is a string that represents the order code.
     * @return The method findByOrderCode is returning an Order object.
     */
    Order findByOrderCode(String code);
}
