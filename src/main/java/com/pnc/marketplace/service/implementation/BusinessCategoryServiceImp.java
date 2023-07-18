package com.pnc.marketplace.service.implementation;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pnc.marketplace.database.BusinessCategoryRepository;
import com.pnc.marketplace.model.seller.BusinessCategory;
import com.pnc.marketplace.service.BusinessCategoryService;
import com.pnc.marketplace.service.FireBaseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BusinessCategoryServiceImp implements BusinessCategoryService {

    @Autowired
    private BusinessCategoryRepository bcRepo;

    @Autowired
    private FireBaseService fBaseService;

    /**
     * This function saves a business category along with its picture.
     * 
     * @param category The category object that needs to be saved. It contains information about the
     * business category such as name, description, etc.
     * @param picture The "picture" parameter is of type MultipartFile, which is a Spring class used
     * for handling file uploads. It represents a file that has been uploaded by the user. In this
     * case, it is used to upload a picture file for a business category.
     * @return The method is returning a BusinessCategory object.
     */
    @Override
    public BusinessCategory saveCategory(BusinessCategory category, MultipartFile picture) {
        try {

            category.setPicture(this.fBaseService.saveFile(picture.getOriginalFilename(), picture.getInputStream(),
                    picture.getContentType()));

            BusinessCategory response = this.bcRepo.save(category);

            if (response == null) {
                log.error("Error in Saving Category");
                return response;
            }
            return response;
        } catch (IOException e) {
            log.error("Error in saving category picture");
            return null;
        }
    }

    /**
     * This function retrieves a business category by its ID from a repository and returns it, or logs
     * an error if the category is not found.
     * 
     * @param id The id parameter is an integer that represents the unique identifier of the business
     * category that we want to retrieve.
     * @return The method is returning an instance of the BusinessCategory class.
     */
    @Override
    public BusinessCategory getCategoryById(int id) {

        BusinessCategory response = this.bcRepo.findById(id).orElse(null);

        if(response != null)
            return response;

        log.error("Error in fetcing category By Id {}",id );
        return null;
    }

    @Override
    public BusinessCategory getCategoryByName(String name) {
        BusinessCategory response = this.bcRepo.findByCategoryName(name);

        if(response != null)
            return response;

        log.error("Error in fetcing category By Name {}",name );
        return null;
    }

    @Override
    public List<BusinessCategory> getAllCategories() {
       
        List<BusinessCategory> response = this.bcRepo.findAll();

        if(response!=null && !response.isEmpty())
            return response;
        
        log.error("Error in fetching categories");
        return response;
    }
}