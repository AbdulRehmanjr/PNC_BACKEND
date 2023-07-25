package com.pnc.marketplace.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.model.seller.SellerRequest;

public interface SellerRequestService {
    
    /**
     * The function saves a seller request along with a file and a picture.
     * 
     * @param sellerRequest The seller request object that contains information about the seller.
     * @param file The "file" parameter is of type MultipartFile and represents a file that needs to be
     * saved along with the seller request. It could be an image, document, or any other type of file.
     * @param picture The "picture" parameter is of type MultipartFile, which is a representation of an
     * uploaded file in a Spring application. It allows you to access the contents of the uploaded
     * file, such as its name, size, and actual data. In this case, it is used to upload a picture file
     * for
     * @return The method is returning a SellerRequest object.
     */
    SellerRequest saveSellerRequest(SellerRequest sellerRequest,MultipartFile file,MultipartFile picture);

    /**
     * The function getById(int id) retrieves a SellerRequest object by its id.
     * 
     * @param id The id parameter is an integer that represents the unique identifier of a seller
     * request.
     * @return The method getById(int id) is returning a SellerRequest object.
     */
    SellerRequest getById(int id);

    /**
     * The function getByUserId retrieves a SellerRequest object based on the given userId.
     * 
     * @param userId An integer representing the unique identifier of a user.
     * @return The method is returning a SellerRequest object.
     */
    SellerRequest getByUserId(String userId);

    /**
     * The function getAllRequests returns a list of SellerRequest objects.
     * 
     * @return The method getAllRequests() returns a list of SellerRequest objects.
     */
    List<SellerRequest> getAllRequests();


    /**
     * The function getByEmail retrieves a seller request object based on the provided email.
     * 
     * @param email A string representing the email address of the seller.
     * @return The method getByEmail is returning a SellerRequest object.
     */
    SellerRequest getByEmail(String email);


    /**
     * The acceptRequest function accepts a seller request with the given ID and returns the
     * corresponding SellerRequest object.
     * 
     * @param id The unique identifier of the seller request that needs to be accepted.
     * @return The method acceptRequest is returning an object of type SellerRequest.
     */
    SellerRequest acceptRequest(int sellerId);

    
    /**
     * The function rejectRequest takes a sellerId and a message as input and returns a SellerRequest
     * object.
     * 
     * @param sellerId An integer representing the ID of the seller who made the request.
     * @param message A string containing the reason for rejecting the request.
     * @return The method rejectRequest returns a SellerRequest object.
     */
    SellerRequest rejectRequest(int sellerId,String message);

    /**
     * The function "updateSeller" takes a SellerRequest object as input and returns a SellerRequest
     * object.
     * 
     * @param sellerRequest The sellerRequest parameter is an object of type SellerRequest, which
     * contains the information needed to update a seller.
     * @return The method updateSeller is returning an object of type SellerRequest.
     */
    SellerRequest updateSeller(SellerRequest sellerRequest);


    /**
     * The function countPendingRequests() returns the number of pending requests.
     * 
     * @return The count of pending requests.
     */
    long countPendingRequests();
}
