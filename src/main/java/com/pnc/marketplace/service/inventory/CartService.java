package com.pnc.marketplace.service.inventory;

import java.util.List;

import com.pnc.marketplace.model.Inventory.Cart;
import com.pnc.marketplace.model.Inventory.CartItem;

public interface CartService {
    
   
    /**
     * This function creates a cart with the given items and associates it with the specified user
     * email.
     * 
     * @param items An array of CartItem objects representing the items to be added to the cart.
     * @param userEmail The email address of the user who is creating the cart.
     * @return The method is returning a Cart object.
     */
    Cart createCart(CartItem[] items,String userEmail);

    /**
     * The createCart function creates a new cart and returns it.
     * 
     * @param cart The parameter "cart" is of type "Cart", which represents a shopping cart object.
     * @return The method is returning a Cart object.
     */
    Cart createCart(Cart cart);

    /**
     * The function updateCart takes a Cart object as input and returns an updated Cart object.
     * 
     * @param cart The "cart" parameter is an object of type "Cart". It represents the shopping cart
     * that needs to be updated.
     * @return The method updateCart is returning a Cart object.
     */
    Cart updateCart(Cart cart);

    /**
     * The function fetchCartById retrieves a cart object based on its ID.
     * 
     * @param cartId A long value representing the unique identifier of the cart.
     * @return The method fetchCartById is returning an object of type Cart.
     */
    Cart fetchCartById(long cartId);


    /**
     * The function fetches a list of carts associated with a user's email.
     * 
     * @param userEmail The email address of the user for whom we want to fetch the cart.
     * @return The method fetchCartByUserEmail is returning a List of Cart objects.
     */
    List<Cart> fetchCartByUserEmail(String userEmail);
}
