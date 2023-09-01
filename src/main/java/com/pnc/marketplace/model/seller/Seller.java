package com.pnc.marketplace.model.seller;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pnc.marketplace.model.Inventory.Product;
import com.pnc.marketplace.model.communication.ChatUserList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @ManyToOne
    private BusinessCategory category;

    private String phone;
    
    private Boolean isActive = false;

    private String sellerType = "NONE";

    private int maxProducts=0;

    private int currentProducts=0;

    @JsonIgnore
    @OneToMany(mappedBy = "seller")
    private List<Product> products;

    @JsonIgnore
    @OneToOne(mappedBy = "seller")
    private ChatUserList chatUserList;

    
}
