package com.pnc.marketplace.implementation.seller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.database.seller.SellerRequestRepository;
import com.pnc.marketplace.model.seller.BusinessCategory;
import com.pnc.marketplace.model.seller.Seller;
import com.pnc.marketplace.model.seller.SellerRequest;
import com.pnc.marketplace.service.firebase.FireBaseService;
import com.pnc.marketplace.service.seller.BusinessCategoryService;
import com.pnc.marketplace.service.seller.SellerRequestService;
import com.pnc.marketplace.service.seller.SellerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerRequestServiceImp implements SellerRequestService {

    @Autowired
    private SellerRequestRepository srRepo;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private BusinessCategoryService bcService;

    @Autowired
    private FireBaseService fbService;

    /**
     * The function saves a SellerRequest object along with two files (document and
     * picture) to a
     * database and returns the saved SellerRequest object.
     * 
     * @param sellerRequest The sellerRequest parameter is an object of type
     *                      SellerRequest, which
     *                      contains information about a seller's request. This
     *                      object is passed as a parameter to the
     *                      method and is used to save the seller request in the
     *                      database.
     * @param file          The "file" parameter is of type MultipartFile and
     *                      represents the file that contains
     *                      the document for the seller request.
     * @param picture       The "picture" parameter is of type MultipartFile, which
     *                      is a representation of an
     *                      uploaded file received in a multipart request. It
     *                      contains the details and content of the
     *                      picture file uploaded by the user.
     * @return The method is returning a SellerRequest object.
     */
    @Override
    public SellerRequest saveSellerRequest(SellerRequest sellerRequest, MultipartFile file, MultipartFile picture) {

        try {
            sellerRequest.setDocument(
                    this.fbService.saveFile(file.getOriginalFilename() + "-document", file.getInputStream(),
                            file.getContentType()));
            sellerRequest.setPicture(this.fbService.saveFile(picture.getOriginalFilename(), picture.getInputStream(),
                    picture.getContentType()));
        } catch (IOException e) {
            log.error("Error {}, Message {}", e.getClass(), e.getMessage());
            return null;
        }

        SellerRequest response = this.srRepo.save(sellerRequest);

        if (response != null) {
            return response;
        }

        log.error("Error in Saving Seller Request");
        return null;

    }

    /**
     * The function retrieves a SellerRequest object by its ID from the repository
     * and returns it, or
     * logs an error and returns null if the object is not found.
     * 
     * @param id The id parameter is an integer that represents the unique
     *           identifier of the seller
     *           request that needs to be fetched.
     * @return The method is returning a SellerRequest object.
     */
    @Override
    public SellerRequest getById(int id) {

        SellerRequest response = this.srRepo.findById(id).orElse(null);

        if (response != null)
            return response;

        log.error("Error in fetching seller request by Id {}", id);
        return null;
    }

    /**
     * The function retrieves all seller requests from the repository and returns
     * them in a list, or
     * logs an error if there is an issue.
     * 
     * @return The method is returning a List of SellerRequest objects.
     */
    @Override
    public List<SellerRequest> getAllRequests() {
        List<SellerRequest> response = this.srRepo.findAll();

        if (response != null)
            return response;

        log.error("Error in fetching all seller request");
        return null;
    }

    /**
     * The function retrieves a seller request object from the repository based on
     * the provided email,
     * and logs an error if the request is not found.
     * 
     * @param email The email parameter is a String that represents the email
     *              address of the seller
     *              request that we want to retrieve.
     * @return The method is returning a SellerRequest object.
     */
    @Override
    public SellerRequest getByEmail(String email) {

        SellerRequest response = this.srRepo.findByEmail(email);

        if (response != null)
            return response;

        log.error("Error in fetching seller request by email {}", email);
        return null;
    }


    // The `@Override` annotation is used to indicate that the method is overriding a method from its
    // superclass or implementing an interface method. In this case, the `acceptRequest` method is
    // overriding a method from the `SellerRequestService` interface.
    @Override
    public SellerRequest acceptRequest(int sellerId) {

        SellerRequest response = this.srRepo.findById(sellerId).orElse(null);

        if (response == null) {
            log.error("No seller found with given Id {}", sellerId);
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
                """, response.getRequestId(), response.getFirstName(), response.getLastName(), response.getCategory(),
                response.getBusinessName());
        response.setRemarks(remarks);

        response = this.srRepo.save(response);

        if (response != null){
            //* saving seller 
            Seller seller = new Seller();

            seller.setFirstName(response.getFirstName());
            seller.setLastName(response.getLastName());
            seller.setAddress(response.getAddress());
            seller.setPicture(response.getPicture());
            seller.setEmail(response.getEmail());

            BusinessCategory category = this.bcService.getCategoryByName(response.getCategory());

            seller.setCategory(category);

            try {
                seller = this.sellerService.saveSeller(seller);
                return response;
            } catch (Exception e) {
                log.error("Seller Saving Error");
                log.error("Error in updating the request with Id {}", sellerId);
                return null;
            }
        }
       return null;
    }

    // The `rejectRequest` method is used to reject a seller request. It takes two
    // parameters:
    // `sellerId` (an integer representing the unique identifier of the seller
    // request) and `message`
    // (a string representing the rejection message).
    @Override
    public SellerRequest rejectRequest(int sellerId, String message) {

        SellerRequest response = this.srRepo.findById(sellerId).orElse(null);

        if (response == null) {
            log.error("No seller found with given Id {}", sellerId);
            return null;
        }

        response.setAccepted(false);
        response.setRemarks(message);
        response.setRejected(true);

        response = this.srRepo.save(response);

        if (response != null)
            return response;

        log.error("Error in updating the request with Id {}", sellerId);
        return null;
    }

    // The `countPendingRequests()` method is used to count the number of pending
    // seller requests in
    // the repository. It uses the `countByIsAcceptedFalse()` method from the
    // `SellerRequestRepository`
    // to count the number of seller requests where the `isAccepted` field is set to
    // `false`. This
    // method returns the count of pending requests as a `long` value.
    @Override
    public long countPendingRequests() {
        return this.srRepo.countByIsAcceptedFalseAndIsRejectedFalse();
    }

    // The `getByUserId` method is used to retrieve a `SellerRequest` object from
    // the repository based
    // on the provided `userId`.
    @Override
    public SellerRequest getByUserId(String userId) {

        SellerRequest response = this.srRepo.findByUserId(userId);

        if (response != null)
            return response;

        log.error("Error in fetching request by User Id {}", userId);
        return null;
    }

    @Override
    public SellerRequest updateSeller(SellerRequest sellerRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSeller'");
    }

}
