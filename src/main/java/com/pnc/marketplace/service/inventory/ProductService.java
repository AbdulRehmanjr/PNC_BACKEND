package com.pnc.marketplace.service.inventory;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.model.Inventory.Product;

public interface ProductService {
    
    /**
     * The function saves a product with its pictures.
     * 
     * @param pictures An array of MultipartFile objects representing the pictures of the product.
     * @param product The product object that needs to be saved. It contains information such as the
     * product name, description, price, etc.
     * @return The method is returning a Product object.
     */
    Product saveProduct(MultipartFile[] pictures,Product product);

    /**
     * The function getProductById returns a Product object based on the given productId.
     * 
     * @param productId A unique identifier for the product.
     * @return The method is returning a product object with the specified product ID.
     */
    Product getProductById(long productId);

    /**
     * The function getProductByCode takes a code as input and returns a Product object.
     * 
     * @param code A string representing the code of the product.
     * @return The method is returning a Product object.
     */
    Product getProductByCode(String code);

    /**
     * The function getProductBySeller takes a sellerId as input and returns the product associated
     * with that seller.
     * 
     * @param sellerId An integer representing the ID of the seller.
     * @return The method is returning a Product object.
     */
    List<Product> getProductsBySeller(int sellerId);

    /**
     * The function returns a list of products based on a given category ID.
     * 
     * @param categoryId The category ID is an integer value that represents the unique identifier of a
     * category. It is used to filter and retrieve products that belong to a specific category.
     * @return The method is returning a list of products that belong to a specific category.
     */
    List<Product> getProductsByCategory(int categoryId);

    /**
     * The function returns a list of products based on the given category name.
     * 
     * @param categoryName A string representing the name of the category for which you want to
     * retrieve the products.
     * @return The method is returning a list of products that belong to the specified category name.
     */
    List<Product> getProductByCategoryName(String categoryName);

    /**
     * The function updateProduct takes a Product object as input and returns a modified version of the
     * same Product object.
     * 
     * @param product The product parameter is an object of type Product. It represents the product
     * that needs to be updated.
     * @return The updated product is being returned.
     */
    Product updateProduct(Product product);

    /**
     * The deleteProduct function is used to remove a product from a system.
     * 
     * @param product The product parameter is of type Product, which represents a product object.
     */
    void deleteProduct(Product product);
    
}
