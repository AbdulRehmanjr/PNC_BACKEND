package com.pnc.marketplace.implementation.inventory;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.database.inventory.ProductRepository;
import com.pnc.marketplace.model.Inventory.Product;
import com.pnc.marketplace.model.seller.BusinessCategory;
import com.pnc.marketplace.model.seller.Seller;
import com.pnc.marketplace.service.firebase.FireBaseService;
import com.pnc.marketplace.service.inventory.ProductService;
import com.pnc.marketplace.service.seller.BusinessCategoryService;
import com.pnc.marketplace.service.seller.SellerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImp  implements ProductService{


    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private FireBaseService fireBaseService;

    @Autowired
    private SellerService sellerService;
    
    @Autowired
    private BusinessCategoryService businessCategoryService;


    @Override
    public Product saveProduct(MultipartFile[] pictures, Product product) {

        Seller seller = this.sellerService.getSellerById(product.getSeller().getSellerId());
        BusinessCategory category = this.businessCategoryService.getCategoryById(product.getCategory().getCategoryId());
        
        if(seller==null)
            return null;
        
        List<String> urls = new ArrayList<>();
        
        
            try {
                urls.add(this.fireBaseService.saveFile(pictures[0].getName()+product.getSeller().getSellerId()+Math.random(), pictures[0].getInputStream(), pictures[0].getContentType()));
                urls.add(this.fireBaseService.saveFile(pictures[1].getName()+product.getSeller().getSellerId()+Math.random(), pictures[1].getInputStream(), pictures[1].getContentType()));
            } catch (IOException e) {
                log.error("Error in Reading Input Stream");
            }
        
        log.info("URLS : {}",urls);
        product.setImages(urls);
        product.setSeller(seller);
        product.setCode(this.generateProductCode(6));
        product.setCategory(category);

        product = this.productRepository.save(product);

        seller.setCurrentProducts(seller.getCurrentProducts()+1);
        this.sellerService.saveSeller(seller);
        
        if(product!=null){
            log.info("Produc Saved");
            return product;
        }
            
        log.error("Error in saving product");
        return null;
    }

    @Override
    public Product getProductById(long productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductById'");
    }

    @Override
    public Product getProductByCode(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductByCode'");
    }

    @Override
    public List<Product> getProductsBySeller(int sellerId) {
       
        List<Product> products = this.productRepository.findBySellerSellerId(sellerId);

        if(products.isEmpty())
            return null;
        return products;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductsByCategory'");
    }

    @Override
    public Product updateProduct(Product product) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
    }

    @Override
    public void deleteProduct(Product product) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProduct'");
    }
    

    private String generateProductCode(int length) {
        final SecureRandom RANDOM = new SecureRandom();
        final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(ALPHANUMERIC_STRING.charAt(RANDOM.nextInt(ALPHANUMERIC_STRING.length())));
        }
        return builder.toString();
    }
}
