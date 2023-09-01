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
    
    @GetMapping(value="/seller/{sellerId}")
    public ResponseEntity<?> getAllProductsBySeller(@PathVariable int sellerId) {

        List<Product> products = this.productService.getProductsBySeller(sellerId);

        if(products != null)
            return ResponseEntity.status(201).body(products);
        
        log.error("Can't Find any product by seller {}",sellerId);
        return null;
    }
    
}
