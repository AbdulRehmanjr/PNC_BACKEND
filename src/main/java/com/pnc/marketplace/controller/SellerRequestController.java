package com.pnc.marketplace.controller;

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
import com.pnc.marketplace.model.seller.SellerRequest;
import com.pnc.marketplace.service.SellerRequestService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sellerrequest")
public class SellerRequestController {

    @Autowired
    private SellerRequestService sRService;

    /**
     * This function handles a POST request to create a seller request, mapping the
     * request string to a
     * SellerRequest object and saving the request along with a file and picture.
     * 
     * @param request A string representation of a JSON object containing the seller
     *                request details.
     * @param file    The "file" parameter is of type MultipartFile, which is used
     *                to handle file uploads
     *                in Spring. It represents a file that is being uploaded by the
     *                client.
     * @param picture The "picture" parameter is of type MultipartFile, which means
     *                it is used to
     *                upload a file from the client-side. In this case, it is used
     *                to upload a picture file.
     * @return The method is returning a ResponseEntity object with a null body.
     */
    @PostMapping("/create")
    ResponseEntity<?> createRequest(@RequestParam("document") MultipartFile file,@RequestParam("picture") MultipartFile picture,String request) {

        SellerRequest req = new SellerRequest();
        ObjectMapper mapper = new ObjectMapper();

        // * mapping the string to seller request object
        try {
            req = mapper.readValue(request, SellerRequest.class);
            req = this.sRService.saveSellerRequest(req, file, picture);
            return ResponseEntity.status(201).body(req);
        } catch (JsonProcessingException e) {
            log.error("Error {} Message {}", e.getCause(), e.getMessage());
            log.error("Error in Saving seller request.");
            return ResponseEntity.ok().body(null);
        }
    }

    /**
     * This function fetches a seller request by its ID and returns a response
     * entity with the seller
     * request if found, or an error message if not found.
     * 
     * @param sellerId The sellerId is a path variable that represents the unique
     *                 identifier of a
     *                 seller. It is used to fetch a specific seller request from
     *                 the database.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{sellerId}")
    ResponseEntity<?> fetchOneById(@PathVariable int sellerId) {

        SellerRequest respone = this.sRService.getById(sellerId);

        if (respone != null)
            return ResponseEntity.status(201).body(respone);

        log.error("Error in fetching seller request by Id {}", sellerId);
        return ResponseEntity.status(404).body(null);
    }

    /**
     * The function fetchOneSellerRequest retrieves a seller request by email and
     * returns it in a
     * ResponseEntity.
     * 
     * @param email The email parameter is a path variable that represents the email
     *              of the seller.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/email/{sellerId}")
    ResponseEntity<?> fetchOneByEmail(@PathVariable String email) {

        SellerRequest respone = this.sRService.getByEmail(email);

        if (respone != null)
            return ResponseEntity.status(201).body(respone);

        log.error("Error in fetching seller request by email {}", email);
        return ResponseEntity.status(404).body(null);
    }

    /**
     * The function fetches all seller requests and returns a response entity with the list of requests
     * if successful, or an error message if unsuccessful.
     * 
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/all")
    ResponseEntity<?> fetchAll() {

        List<SellerRequest> respone = this.sRService.getAllRequests();

        if (respone != null)
            return ResponseEntity.status(201).body(respone);

        log.error("Error in fetching seller requests");
        return ResponseEntity.status(404).body(null);
    }

    /**
     * The function accepts a seller request and returns a response entity with the
     * accepted request if
     * successful, or an error message if not.
     * 
     * @param sellerId The sellerId parameter is the unique identifier of the seller
     *                 request that needs
     *                 to be accepted by the admin.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/accept/{sellerId}")
    ResponseEntity<?> acceptRequest(@PathVariable int sellerId) {
        SellerRequest respone = this.sRService.acceptRequest(sellerId);

        if (respone != null)
            return ResponseEntity.status(201).body(respone);

        log.error("Error in accepting seller Request by Admin", sellerId);
        return ResponseEntity.status(404).body(null);
    }

    /**
     * This function handles the rejection of a seller request by an admin.
     * 
     * @param sellerId The sellerId is the unique identifier of the seller whose request is being
     * rejected.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/reject/{sellerId}")
    ResponseEntity<?> rejectRequest(@PathVariable int sellerId,@RequestBody String message) {
        SellerRequest respone = this.sRService.rejectRequest(sellerId,message);

        if (respone != null)
            return ResponseEntity.status(201).body(respone);

        log.error("Error in rejecting seller Request by Admin", sellerId);
        return ResponseEntity.status(404).body(null);
    }

   /**
    * The function "pendingRequestsCount" returns the count of pending requests as a response entity.
    * 
    * @return The method is returning a ResponseEntity object.
    */
    @GetMapping("/pending")
    ResponseEntity<?> pendingRequestsCount(){

        long count = this.sRService.countPendingRequests();
        return ResponseEntity.status(201).body(count);
    }

    /**
     * This function retrieves a SellerRequest object by the given userId and returns it as a
     * ResponseEntity.
     * 
     * @param userId The userId is a path variable that represents the unique identifier of a user.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/check-requested/{userId}")
    ResponseEntity<?> checkRequest(@PathVariable String userId){

        SellerRequest response = this.sRService.getByUserId(userId);

        return ResponseEntity.ok(response);
    }
}
