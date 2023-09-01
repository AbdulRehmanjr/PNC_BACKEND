package com.pnc.marketplace.database.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.Inventory.Product;

public interface ProductRepository extends JpaRepository<Product,Long>{
    
}
