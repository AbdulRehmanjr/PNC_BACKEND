package com.pnc.marketplace.database.seller;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.seller.BusinessCategory;

public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory,Integer>{
    
    /**
     * The function findByCategoryName takes a category name as input and returns a BusinessCategory
     * object.
     * 
     * @param name A String representing the name of the category to search for.
     * @return The method findByCategoryName returns a BusinessCategory object.
     */
    BusinessCategory findByCategoryName(String name);
}
