package com.pnc.marketplace.implementation.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.database.inventory.CartItemRepository;
import com.pnc.marketplace.database.inventory.CartRepository;
import com.pnc.marketplace.model.Inventory.Cart;
import com.pnc.marketplace.model.Inventory.CartItem;
import com.pnc.marketplace.service.inventory.CartService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    /**
     * The function creates a cart for a user, saves it to the database, and associates cart items with
     * the cart.
     * 
     * @param items An array of CartItem objects representing the items to be added to the cart.
     * @param userEmail The userEmail parameter is a String that represents the email address of the
     * user for whom the cart is being created.
     * @return The method is returning a Cart object.
     */
    @Override
    public Cart createCart(CartItem[] items, String userEmail) {
        log.info("Saving cart of user : {}", userEmail);

        Cart cart = new Cart();
        cart.setUserEmail(userEmail);
        cart = this.cartRepo.save(cart);

        for (CartItem item : items) {
            item.setCart(cart);
            this.cartItemRepo.save(item);
        }

        if (cart == null)
            return null;
        return cart;
    }

    /**
     * The function creates a cart for a user, saves it to the database, and also saves the cart items
     * associated with the cart.
     * 
     * @param cart The "cart" parameter is an object of type Cart, which represents a shopping cart. It
     * contains information such as the user's email and a list of cart items.
     * @return The method is returning a Cart object.
     */
    @Override
    public Cart createCart(Cart cart) {
        log.info("Saving cart of user : {}", cart.getUserEmail());

        try {
            Cart response = this.cartRepo.save(cart);

            try {
                cart.getCartItems().forEach(
                        item -> {
                            item.setCart(response);
                            this.cartItemRepo.save(item);
                        });
            } catch (Exception e) {
                log.error("Item Error : {}", e.getMessage());
            }

            if (response == null)
                return null;
            return response;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * The function fetches a cart object from the cart repository based on the given cartId.
     * 
     * @param cartId The cartId is the unique identifier of the cart that needs to be fetched.
     * @return The method is returning a Cart object.
     */
    @Override
    public Cart fetchCartById(long cartId) {
      
        Cart response = this.cartRepo.findById(cartId).orElse(null);

        if(response!=null)
            return response;
        
        log.info("No cart FOund with given Id: {}",cartId);
        return null;
    }

    @Override
    public List<Cart> fetchCartByUserEmail(String userEmail) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchCartByUserEmail'");
    }

}
