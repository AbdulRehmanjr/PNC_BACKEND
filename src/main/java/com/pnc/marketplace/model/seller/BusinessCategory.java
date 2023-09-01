package com.pnc.marketplace.model.seller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pnc.marketplace.model.Inventory.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BusinessCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    private String categoryName;

    private String picture;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Seller> sellers;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
