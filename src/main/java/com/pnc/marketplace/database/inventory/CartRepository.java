package com.pnc.marketplace.database.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.Inventory.Cart;

public interface  CartRepository extends JpaRepository<Cart,Long>{

    /**
     * The function findByUserEmailAndIsPaidFalse searches for a cart by user email where the cart is
     * not paid.
     * 
     * @param userEmail The email address of the user for whom we want to find the cart.
     * @return The method findByUserEmailAndIsPaidFalse returns a Cart object.
     */
    Cart findByUserEmailAndIsPaidFalse(String userEmail);
}
