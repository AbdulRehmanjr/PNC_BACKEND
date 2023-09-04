package com.pnc.marketplace.controller.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnc.marketplace.model.Inventory.Product;
import com.pnc.marketplace.service.inventory.ProductService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    
    /**
     * The function saves a product with pictures and returns a response entity with the saved product.
     * 
     * @param pictures An array of MultipartFile objects representing the pictures of the product being
     * saved.
     * @param productStr The parameter `productStr` is a string representation of a JSON object that
     * contains the details of a product. It is used to deserialize the JSON object into a `Product`
     * object using the `ObjectMapper` class.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/save")
    ResponseEntity<?> saveProduct(@RequestParam("pictures") MultipartFile[] pictures, String productStr){
        
        Product product = new Product();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            log.info("Product : {}",productStr);
            product = objectMapper.readValue(productStr,Product.class);
        } catch (JsonProcessingException e) {
          log.error("Error : {}",e.getMessage());
          return null;
        }

        product = this.productService.saveProduct(pictures, product);

        if(product!=null)
            return ResponseEntity.status(201).body(product);
        return ResponseEntity.status(404).body(null);
    }  
    
    /**
     * This function retrieves all products associated with a specific seller.
     * 
     * @param sellerId The sellerId is a path variable that represents the unique identifier of a
     * seller. It is used to retrieve all the products associated with that seller.
     * @return The method is returning a ResponseEntity object. If the products list is not null, it
     * will return a ResponseEntity with a status code of 201 (created) and the products list as the
     * response body. If the products list is null, it will log an error message and return null.
     */
    @GetMapping(value="/seller/{sellerId}")
    public ResponseEntity<?> getAllProductsBySeller(@PathVariable int sellerId) {

        List<Product> products = this.productService.getProductsBySeller(sellerId);

        if(products != null)
            return ResponseEntity.status(201).body(products);
        
        log.error("Can't Find any product by seller {}",sellerId);
        return null;
    }

     /**
      * The function retrieves all products based on a given category name and returns them in a
      * ResponseEntity.
      * 
      * @param categoryName The categoryName parameter is a String that represents the name of the
      * category for which we want to retrieve the products.
      * @return The method is returning a ResponseEntity object. If the products list is not null, it
      * will return a ResponseEntity with a status code of 201 (created) and the products list as the
      * response body. If the products list is null, it will log an error message and return null.
      */
     @GetMapping(value="/category/{categoryName}")
    public ResponseEntity<?> getAllProductsByCategoryName(@PathVariable String categoryName) {

        List<Product> products = this.productService.getProductByCategoryName(categoryName);

        if(products != null)
            return ResponseEntity.status(201).body(products);
        
        log.error("Can't Find any product by category {}",categoryName);
        return null;
    }
    
}
