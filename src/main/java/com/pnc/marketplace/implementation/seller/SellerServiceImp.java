package com.pnc.marketplace.implementation.seller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.database.seller.SellerRepository;
import com.pnc.marketplace.model.seller.Seller;
import com.pnc.marketplace.service.firebase.FireBaseService;
import com.pnc.marketplace.service.seller.SellerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerServiceImp implements SellerService {

    @Autowired
    private SellerRepository sellerRepo;

    @Autowired
    private FireBaseService fbService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    

    /**
     * The function saves a seller object along with a picture file and returns the
     * saved seller
     * object.
     * 
     * @param seller  The "seller" parameter is an object of type Seller, which
     *                represents a seller
     *                entity in the system. It contains information about the seller
     *                such as name, address, contact
     *                details, etc.
     * @param picture The "picture" parameter is of type MultipartFile, which is a
     *                representation of an
     *                uploaded file received in a multipart request. It contains
     *                information about the file, such as
     *                its original filename, content type, and the actual file
     *                content as an InputStream.
     * @return The method is returning a Seller object.
     */
    @Override
    public Seller saveSeller(Seller seller, MultipartFile picture) {

        try {
            seller.setPicture(this.fbService.saveFile(picture.getOriginalFilename(), picture.getInputStream(),
                    picture.getContentType()));

            Seller response = this.sellerRepo.save(seller);

            if (response != null)
                return response;

        } catch (Exception e) {
            log.error("Error in saving Seller");
        }
        return null;
    }

    /**
     * The function saves a seller object and returns the saved seller if successful, otherwise it logs
     * an error and returns null.
     * 
     * @param seller The parameter "seller" is an object of type "Seller" that represents the seller to
     * be saved.
     * @return The method is returning a Seller object.
     */
    @Override
    public Seller saveSeller(Seller seller) {

        Seller response = this.sellerRepo.save(seller);

        if (response != null)
            return response;

        log.error("Error in saving seller");
        return null;
    }

    /**
     * The function getSellerById retrieves a Seller object from the sellerRepo
     * based on the given id,
     * and returns it if found, otherwise logs an error and returns null.
     * 
     * @param id The id parameter is an integer that represents the unique
     *           identifier of the seller.
     * @return The method is returning a Seller object.
     */
    @Override
    public Seller getSellerById(int id) {

        Seller response = this.sellerRepo.findById(id).orElse(null);

        if (response != null)
            return response;

        log.error("Error in fetching seller By Id {}", id);
        return response;
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller response = this.sellerRepo.findByEmail(email);

        if (response != null)
            return response;

        log.error("Error in fetching seller By Email {}", email);
        return response;
    }

    /**
     * The function getAllSellers retrieves a list of sellers from the seller
     * repository and returns
     * it, logging an error if the list is empty or null.
     * 
     * @return The method is returning a List of Seller objects.
     */
    @Override
    public List<Seller> getAllSellers() {

        List<Seller> response = this.sellerRepo.findAll();

        if (response != null && !response.isEmpty())
            return response;

        log.error("Error in Sellers list");
        return response;
    }
}
