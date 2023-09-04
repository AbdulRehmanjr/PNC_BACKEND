package com.pnc.marketplace.database.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.Inventory.Cart;

public interface  CartRepository extends JpaRepository<Cart,Long>{
    
}
