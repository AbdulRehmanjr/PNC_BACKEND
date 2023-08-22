package com.pnc.marketplace.database.seller;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.seller.SellerDashboard;

public interface SellerDashboardRepository extends JpaRepository<SellerDashboard,Integer>{
    
}
