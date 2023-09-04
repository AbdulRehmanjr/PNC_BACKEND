package com.pnc.marketplace.model.Inventory;

import java.util.HashSet;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    private String userEmail;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private Set<CartItem> cartItems = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "cart")
    private Order order;

    private boolean isPaid = false;

    @Override
    public String toString() {
        return "Cart [cartId=" + cartId + ", userEmail=" + userEmail + "]";
    }
}
