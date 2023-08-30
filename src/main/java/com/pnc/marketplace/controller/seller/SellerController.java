package com.pnc.marketplace.controller.seller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnc.marketplace.model.seller.Seller;
import com.pnc.marketplace.service.seller.SellerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    /**
     * The function creates a new seller by parsing the seller object from JSON, checking if the seller
     * already exists, and saving the seller along with a profile picture.
     * 
     * @param seller The "seller" parameter is a JSON string that represents the seller object. It will
     * be deserialized into a Seller object using the ObjectMapper.
     * @param picture The "picture" parameter is of type MultipartFile, which is used to handle file
     * uploads in Spring MVC. It represents a file that is being uploaded by the client. In this case,
     * it is used to upload a picture file for the seller.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/create")
    ResponseEntity<?> createSeller(String seller, @RequestParam("file") MultipartFile picture) {

       Seller sel = new Seller();

        ObjectMapper mapper = new ObjectMapper();

        try {
            sel = mapper.readValue(seller, Seller.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }

        Seller response = this.sellerService.getSellerByEmail(sel.getEmail());

        if (response != null) {
            log.error("Seller Already Existed with given mail.");
            return ResponseEntity.status(404).body("Seller Already Existed with given mail.");
        }

        sel = this.sellerService.saveSeller(sel, picture);

        if (sel != null)
            return ResponseEntity.status(201).body(sel);

        log.error("Error in saving category");
        return ResponseEntity.status(404).body(null);
    }

    
    /**
     * This function retrieves a seller by their ID and returns a response entity with the seller
     * information if found, or an error message if not found.
     * 
     * @param sellerId The sellerId is a path variable that represents the unique identifier of a
     * seller. It is used to retrieve the seller information from the database.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{sellerId}")
    ResponseEntity<?> findSellerById(@PathVariable int sellerId) {

        Seller response = this.sellerService.getSellerById(sellerId);

        if (response != null)
            return ResponseEntity.status(201).body(response);

        log.error("Error in fetching Seller.");
        return ResponseEntity.status(404).body(null);
    }

    /**
     * This function returns a list of all sellers and handles error cases.
     * 
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/all")
    ResponseEntity<?> findAllSellers() {

        List<Seller> response = this.sellerService.getAllSellers();

        if (response != null)
            return ResponseEntity.status(201).body(response);

        log.error("Error in fetching Seller.");
        return ResponseEntity.status(404).body(null);
    }
   
    /**
     * This Java function retrieves a seller by their email and returns a ResponseEntity with the
     * seller if found, or an error message if not found.
     * 
     * @param email The email parameter is a path variable that represents the email address of a
     * seller.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/email/{email}")
    ResponseEntity<?> findByEmail(@PathVariable String email) {

        Seller response = this.sellerService.getSellerByEmail(email);

        if (response != null)
            return ResponseEntity.status(201).body(response);

        log.error("Error in fetching seller");
        return ResponseEntity.status(404).body(null);
    }   

    /**
     * This function updates the password for a seller with the given email.
     * 
     * @param email The email parameter is a path variable that represents the email address of the
     * user whose password needs to be updated.
     * @param password The password parameter is the new password that the user wants to update.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/update-password/{email}")
    ResponseEntity<?> updatePassword(@PathVariable String email,@RequestBody String password){

        String response = this.sellerService.updatePassword(email, password);

        if(response==null)
            return ResponseEntity.status(404).body("Error in updating password");
        
        return ResponseEntity.status(201).body(response);
    
    }


}
