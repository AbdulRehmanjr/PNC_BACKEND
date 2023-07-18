package com.pnc.marketplace.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.seller.Seller;

public interface SellerRepository extends JpaRepository<Seller,Integer>{
    
    Seller findByEmail(String email);
}
