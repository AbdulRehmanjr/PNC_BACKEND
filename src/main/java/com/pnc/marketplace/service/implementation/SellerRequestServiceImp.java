package com.pnc.marketplace.service.implementation;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.database.SellerRequestRepository;
import com.pnc.marketplace.model.seller.SellerRequest;
import com.pnc.marketplace.service.FireBaseService;
import com.pnc.marketplace.service.SellerRequestService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerRequestServiceImp implements SellerRequestService {

    @Autowired
    private SellerRequestRepository srRepo;

    @Autowired
    private FireBaseService fbService;

    /**
     * The function saves a SellerRequest object along with two files (document and picture) to a
     * database and returns the saved SellerRequest object.
     * 
     * @param sellerRequest The sellerRequest parameter is an object of type SellerRequest, which
     * contains information about a seller's request. This object is passed as a parameter to the
     * method and is used to save the seller request in the database.
     * @param file The "file" parameter is of type MultipartFile and represents the file that contains
     * the document for the seller request.
     * @param picture The "picture" parameter is of type MultipartFile, which is a representation of an
     * uploaded file received in a multipart request. It contains the details and content of the
     * picture file uploaded by the user.
     * @return The method is returning a SellerRequest object.
     */
    @Override
    public SellerRequest saveSellerRequest(SellerRequest sellerRequest,MultipartFile file,MultipartFile picture) {
        
        try {
            sellerRequest.setDocument(this.fbService.saveFile(file.getOriginalFilename(), file.getInputStream(), file.getContentType()));
            sellerRequest.setPicture(this.fbService.saveFile(picture.getOriginalFilename(), picture.getInputStream(), picture.getContentType()));
        } catch (IOException e) {
            log.error("Error {}, Message {}",e.getClass(),e.getMessage());
            return null;
        }

        SellerRequest response = this.srRepo.save(sellerRequest);
        if(response!=null)
            return response;

        log.error("Error in Saving Seller Request");
        return null;

    }

    /**
     * The function retrieves a SellerRequest object by its ID from the repository and returns it, or
     * logs an error and returns null if the object is not found.
     * 
     * @param id The id parameter is an integer that represents the unique identifier of the seller
     * request that needs to be fetched.
     * @return The method is returning a SellerRequest object.
     */
    @Override
    public SellerRequest getById(int id) {
        
        SellerRequest response = this.srRepo.findById(id).orElse(null);

        if(response!=null)
            return response;
        
        log.error("Error in fetching seller request by Id {}",id);
        return null;
    }

    /**
     * The function retrieves all seller requests from the repository and returns them in a list, or
     * logs an error if there is an issue.
     * 
     * @return The method is returning a List of SellerRequest objects.
     */
    @Override
    public List<SellerRequest> getAllRequests() {
        List<SellerRequest> response = this.srRepo.findAll();

        if(response!=null)
            return response;
        
        log.error("Error in fetching all seller request");
        return null;
    }

    /**
     * The function retrieves a seller request object from the repository based on the provided email,
     * and logs an error if the request is not found.
     * 
     * @param email The email parameter is a String that represents the email address of the seller
     * request that we want to retrieve.
     * @return The method is returning a SellerRequest object.
     */
    @Override
    public SellerRequest getByEmail(String email) {

        SellerRequest response = this.srRepo.findByEmail(email);

        if(response!=null)
            return response;
        
        log.error("Error in fetching seller request by email {}",email);
        return null;
    }


    @Override
    public SellerRequest acceptRequest(int sellerId) {
        
        SellerRequest response = this.srRepo.findById(sellerId).orElse(null);

        if(response==null){
            log.error("No seller found with given Id {}",sellerId);
            return null;
        }

        response.setAccepted(true);
        String remarks = String.format("""
                Your Request to become Seller with: 
                Id :%s
                First Name : %s 
                Last Name : %s 
                Business Category : %s
                Business Name : %s 
                has been approved by Administration.
                """, response.getRequestId(), response.getFirstName(),response.getLastName(),response.getCategory(),response.getBusinessName());
        response.setRemarks(remarks);

        response = this.srRepo.save(response);

        if(response!=null)
            return response;
        
        log.error("Error in updating the request with Id {}",sellerId);
        return null;
    }

   
    // The `rejectRequest` method is used to reject a seller request by updating the `accepted` field
    // of the `SellerRequest` object with the given `id` to `false`. It also sets the `remarks` field
    // with a formatted message indicating that the request has been rejected by the administration.
    // The method retrieves the `SellerRequest` object from the repository based on the provided `id`,
    // updates the necessary fields, and saves the updated object back to the repository. If the update
    // is successful, the updated `SellerRequest` object is returned. If there is an error during the
    // update process, an error message is logged and `null` is returned.
    @Override
    public SellerRequest rejectRequest(int sellerId) {
        SellerRequest response = this.srRepo.findById(sellerId).orElse(null);

        if(response==null){
            log.error("No seller found with given Id {}",sellerId);
            return null;
        }

        response.setAccepted(false);
        String remarks = String.format("""
                Your Request to become Seller with: 
                Id :%s
                First Name : %s 
                Last Name : %s 
                Business Category : %s
                Business Name : %s 
                has been rejected by Administration.
                """, response.getRequestId(), response.getFirstName(),response.getLastName(),response.getCategory(),response.getBusinessName());
        response.setRemarks(remarks);

        response = this.srRepo.save(response);

        if(response!=null)
            return response;
        
        log.error("Error in updating the request with Id {}",sellerId);
        return null;
    }

    @Override
    public SellerRequest updateSeller(SellerRequest sellerRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSeller'");
    }
    
}
