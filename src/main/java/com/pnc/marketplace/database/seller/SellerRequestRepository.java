package com.pnc.marketplace.database.seller;

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
     * The function findByUserId takes a userId as input and returns a SellerRequest object.
     * 
     * @param userId A string representing the unique identifier of a user.
     * @return The method findByUserId(String userId) is returning a SellerRequest object.
     */
    SellerRequest findByUserId(String userId);

   
   /**
    * The function counts the number of records where the "isAccepted" and "isRejected" fields are both
    * false.
    * 
    * @return The method `countByIsAcceptedFalseAndIsRejectedFalse()` is returning a `long` value.
    */
 long countByIsAcceptedFalseAndIsRejectedFalse();

}
