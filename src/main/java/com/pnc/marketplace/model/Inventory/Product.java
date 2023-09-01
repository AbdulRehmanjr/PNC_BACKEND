package com.pnc.marketplace.model.Inventory;

import java.util.List;

import com.pnc.marketplace.model.seller.BusinessCategory;
import com.pnc.marketplace.model.seller.Seller;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    private String code;

    private String name;

    private String description;

    private List<String> images;

    private double price;

    private int quantity;

    private int rating=0;

    @ManyToOne
    private BusinessCategory category;

    @ManyToOne
    private Seller seller;
}
