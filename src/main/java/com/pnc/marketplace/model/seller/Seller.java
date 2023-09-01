package com.pnc.marketplace.model.seller;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.pnc.marketplace.model.Inventory.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Seller {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sellerId;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String picture;

    private String address;

    @JsonProperty(access =  Access.WRITE_ONLY)
    private String password;

    @ManyToOne
    private BusinessCategory category;

    private Boolean isActive = false;

    private String sellerType = "NONE";

    private int maxProducts=0;

    private int currentProducts=0;

    @JsonIgnore
    @OneToMany(mappedBy = "seller")
    private List<Product> products;
}
