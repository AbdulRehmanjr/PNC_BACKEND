package com.pnc.marketplace.controller;

import java.util.List;

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
import com.pnc.marketplace.model.seller.BusinessCategory;
import com.pnc.marketplace.service.BusinessCategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/category")
public class BusinessCategoryController {

    @Autowired
    private BusinessCategoryService bcService;

    /**
     * The function creates a new category by parsing the category information from
     * a JSON string and
     * checking if the category already exists.
     * 
     * @param category The "category" parameter is a string that represents the
     *                 category information in
     *                 JSON format. It will be deserialized into a BusinessCategory
     *                 object using the ObjectMapper.
     * @param picture  The "picture" parameter is of type MultipartFile, which is
     *                 used to handle file
     *                 uploads in Spring. It represents a file that is being
     *                 uploaded by the client.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/create")
    ResponseEntity<?> createCategory(String category, @RequestParam("file") MultipartFile picture) {

        BusinessCategory cat = new BusinessCategory();

        ObjectMapper mapper = new ObjectMapper();

        try {
            cat = mapper.readValue(category, BusinessCategory.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }

        BusinessCategory response = this.bcService.getCategoryByName(cat.getCategoryName());

        if (response != null) {
            log.error("Category Already Existed");
            return ResponseEntity.status(404).body("Category Already Existed");
        }

        cat = this.bcService.saveCategory(cat, picture);

        if (cat != null)
            return ResponseEntity.status(201).body(cat);

        log.error("Error in saving category");
        return ResponseEntity.status(404).body(null);
    }

    /**
     * This function retrieves a business category by its ID and returns a response
     * entity with the
     * category if found, or an error message if not found.
     * 
     * @param id The id parameter is the unique identifier of the category that we
     *           want to fetch. It is
     *           passed as a path variable in the URL.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{categoryId}")
    ResponseEntity<?> fetchCategoryById(@PathVariable("categoryId") int id) {

        BusinessCategory response = this.bcService.getCategoryById(id);

        if (response != null)
            return ResponseEntity.status(201).body(response);

        log.error("Error in fetching category");
        return ResponseEntity.status(404).body(null);
    }

    /**
     * This function retrieves a business category by its name and returns a response entity with the
     * category if found, or an error message if not found.
     * 
     * @param name The name parameter is a String that represents the category name.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/name/{categoryName}")
    ResponseEntity<?> fetchByCategoryName(String name) {

        BusinessCategory response = this.bcService.getCategoryByName(name);

        if (response != null)
            return ResponseEntity.status(201).body(response);

        log.error("Error in fetching category");
        return ResponseEntity.status(404).body(null);
    }
    
    /**
     * The function fetches all categories and returns a response entity with the categories if
     * successful, or an error message if unsuccessful.
     * 
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/all")
    ResponseEntity<?> fetchAllCategories() {

        List<BusinessCategory> response = this.bcService.getAllCategories();

        if (response != null)
            return ResponseEntity.status(201).body(response);

        log.error("Error in fetching category");
        return ResponseEntity.status(404).body(null);
    }
    
}
