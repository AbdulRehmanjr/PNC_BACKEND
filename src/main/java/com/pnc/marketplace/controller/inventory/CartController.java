package com.pnc.marketplace.controller.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnc.marketplace.model.Inventory.Cart;
import com.pnc.marketplace.model.Inventory.CartItem;
import com.pnc.marketplace.service.inventory.CartService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping("/save/{userEmail}")
    ResponseEntity<?> saveCartByItems(@RequestBody CartItem[] items,@PathVariable String userEmail){

        Cart response = this.cartService.createCart(items,userEmail);

        if(response == null)
            return ResponseEntity.status(404).body(null);
        
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/save")
    ResponseEntity<?> saveCart(@RequestBody Cart cart){
     
        Cart response = this.cartService.createCart(cart);

        if(response == null)
            return ResponseEntity.status(404).body(null);
        
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{cartId}")
     ResponseEntity<?> getCartById(@PathVariable long cartId){
     
        Cart response = this.cartService.fetchCartById(cartId);

        if(response == null)
            return ResponseEntity.status(404).body(null);
        
        return ResponseEntity.status(201).body(response);
    }

}
