package com.pnc.marketplace.model.seller;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


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

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public BusinessCategory getCategory() {
        return category;
    }

    public void setCategory(BusinessCategory category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Seller [sellerId=" + sellerId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
                + email + ", picture=" + picture + ", address=" + address + ", password=" + password + ", category="
                + category + "]";
    }

}
