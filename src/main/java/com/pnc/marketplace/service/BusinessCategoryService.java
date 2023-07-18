package com.pnc.marketplace.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.model.seller.BusinessCategory;

public interface BusinessCategoryService {

  
    /**
     * The function "savCategory" saves a business category along with a picture.
     * 
     * @param category The category of the business. It could be a string or an enum representing
     * different categories such as "Food", "Retail", "Services", etc.
     * @param picture The picture parameter is of type MultipartFile, which is a class in Spring
     * Framework used to represent a file that has been uploaded via a form. It allows you to access
     * the contents of the file, such as its name, size, and actual data.
     * @return The method `savCategory` is returning a `BusinessCategory` object.
     */
    BusinessCategory saveCategory(BusinessCategory category,MultipartFile picture);

    /**
     * The function getCategoryById takes an integer id as input and returns the BusinessCategory
     * associated with that id.
     * 
     * @param id The id parameter is an integer that represents the unique identifier of a business
     * category.
     * @return The method `getCategoryById` returns an object of type `BusinessCategory`.
     */
    BusinessCategory getCategoryById(int id);

    /**
     * The function getCategoryByName takes a name as input and returns the BusinessCategory object
     * associated with that name.
     * 
     * @param name A string representing the name of the category.
     * @return The method is returning a BusinessCategory object.
     */
    BusinessCategory getCategoryByName(String name);

    /**
     * The function getAllCategories() returns a list of all business categories.
     * 
     * @return The method getAllCategories() returns a list of BusinessCategory objects.
     */
    List<BusinessCategory> getAllCategories();

    
}
