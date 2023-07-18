package com.pnc.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnc.marketplace.model.seller.Seller;
import com.pnc.marketplace.service.SellerService;

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

    
    @GetMapping("/{sellerId}")
    ResponseEntity<?> findSellerById(@PathVariable int sellerId) {

        Seller response = this.sellerService.getSellerById(sellerId);

        if (response != null)
            return ResponseEntity.status(201).body(response);

        log.error("Error in fetching Seller.");
        return ResponseEntity.status(404).body(null);
    }

   
    @GetMapping("/email/{email}")
    ResponseEntity<?> findByCategoryName(String email) {

        Seller response = this.sellerService.getSellerByEmail(email);

        if (response != null)
            return ResponseEntity.status(201).body(response);

        log.error("Error in fetching seller");
        return ResponseEntity.status(404).body(null);
    }   



}
