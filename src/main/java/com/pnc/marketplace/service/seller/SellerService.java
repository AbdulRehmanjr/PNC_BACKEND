package com.pnc.marketplace.service.seller;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.model.seller.Seller;

public interface SellerService {
    
    /**
     * The function saves a seller object along with a picture file.
     * 
     * @param seller The seller object that contains the information of the seller.
     * @param picture The picture parameter is of type MultipartFile, which is a class in Spring
     * Framework used to represent a file that has been uploaded as part of a multipart request. It
     * allows you to access the contents of the file, such as its name, size, and actual data.
     * @return The method is returning a Seller object.
     */
    Seller saveSeller(Seller seller,MultipartFile picture);

    /**
     * The function saves a seller object and returns the saved seller.
     * 
     * @param seller The seller object that needs to be saved.
     * @return The method saveSeller is returning an object of type Seller.
     */
    Seller saveSeller(Seller seller);

    /**
     * The function getSellerById returns a Seller object based on the given id.
     * 
     * @param id An integer representing the unique identifier of the seller.
     * @return The method getSellerById is returning an object of type Seller.
     */
    Seller getSellerById(int id);
    
    /**
     * The function getSellerByEmail takes an email as input and returns a Seller object.
     * 
     * @param email A string representing the email address of the seller.
     * @return The method getSellerByEmail returns a Seller object.
     */
    Seller getSellerByEmail(String email);
    
    /**
     * The function getAllSellers returns a list of all sellers.
     * 
     * @return The method getAllSellers() returns a list of objects of type Seller.
     */
    List<Seller> getAllSellers();

    /**
     * The function updatePassword takes an email as input and returns a string representing the
     * updated password.
     * 
     * @param email A string representing the email address of the user for whom the password needs to
     * be updated.
     * @return The method is returning a String value.
     */
    String updatePassword(String email,String password);
}
