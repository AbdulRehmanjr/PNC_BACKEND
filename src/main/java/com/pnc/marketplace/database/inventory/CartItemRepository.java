package com.pnc.marketplace.database.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.Inventory.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long>{
    
}
