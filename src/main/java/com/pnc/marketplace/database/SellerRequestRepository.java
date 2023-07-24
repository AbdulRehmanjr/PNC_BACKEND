package com.pnc.marketplace.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.seller.SellerRequest;

public interface SellerRequestRepository extends JpaRepository<SellerRequest,Integer> {
    
    /**
     * The function findByEmail in the SellerRepository interface returns a Seller object based on the
     * provided email.
     * 
     * @param email The email parameter is a string that represents the email address of a seller.
     * @return The method `findByEmail` in the `SellerRepository` class is returning an object of type
     * `Seller` that matches the given email.
     */
    SellerRequest findByEmail(String email); 

   /**
    * The function countByIsAcceptedFalse() returns the number of records where the isAccepted field is
    * false.
    * 
    * @return The count of items where the "isAccepted" property is false.
    */
    long countByIsAcceptedFalse();
}
