package com.pnc.marketplace.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.seller.Seller;

public interface SellerRepository extends JpaRepository<Seller,Integer>{
    
    /**
     * The function findByEmail takes an email as input and returns a Seller object that matches the
     * given email.
     * 
     * @param email A string representing the email address of the seller.
     * @return The method findByEmail is returning a Seller object.
     */
    Seller findByEmail(String email);
}
