package com.pnc.marketplace.database.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.Inventory.Product;

public interface ProductRepository extends JpaRepository<Product,Long>{
    
    List<Product> findBySellerSellerId(int sellerId);

    List<Product> findAllByCategoryCategoryName(String categoryName);
}
