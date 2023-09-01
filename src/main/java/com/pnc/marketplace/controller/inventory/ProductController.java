package com.pnc.marketplace.controller.inventory;

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
            product = objectMapper.readValue(productStr,Product.class);
        } catch (JsonProcessingException e) {
          log.error("Error in converting string in Json Object");
          return null;
        }

        product = this.productService.saveProduct(pictures, product);

        if(product!=null)
            return ResponseEntity.status(201).body(product);
        return ResponseEntity.status(404).body(null);
    }   
}
