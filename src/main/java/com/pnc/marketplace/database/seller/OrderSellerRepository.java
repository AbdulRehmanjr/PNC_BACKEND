package com.pnc.marketplace.database.seller;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.seller.OrderSeller;

public interface OrderSellerRepository extends JpaRepository<OrderSeller,Long> {
    
    /**
     * The function finds all orders by a given seller ID.
     * 
     * @param seller An integer representing the ID of the seller.
     * @return The method `findAllBySellerId` returns a list of `OrderSeller` objects.
     */
    List<OrderSeller> findAllBySellerId(int seller);
}
