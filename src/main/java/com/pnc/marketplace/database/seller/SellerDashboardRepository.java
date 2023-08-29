package com.pnc.marketplace.database.seller;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.seller.SellerDashboard;

public interface SellerDashboardRepository extends JpaRepository<SellerDashboard,Integer>{
    

    /**
     * The function findByEmail takes an email as input and returns a SellerDashboard object.
     * 
     * @param email A string representing the email address of the seller.
     * @return The method findByEmail(String email) is returning a SellerDashboard object.
     */
    SellerDashboard findByEmail(String email);
}
