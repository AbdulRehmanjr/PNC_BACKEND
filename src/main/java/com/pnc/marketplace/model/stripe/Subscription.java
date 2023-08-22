package com.pnc.marketplace.model.stripe;

import java.sql.Date;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subId;

    private String customerId;

    private String email;

    @Column(unique=true,nullable = true)
    private String subscriptionId;
    
    @CreationTimestamp
    private Date DateSubcribed;

    private LocalDate DateValid;

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Date getDateSubcribed() {
        return DateSubcribed;
    }

    public void setDateSubcribed(Date dateSubcribed) {
        DateSubcribed = dateSubcribed;
    }

    public LocalDate getDateValid() {
        return DateValid;
    }

    public void setDateValid(LocalDate dateValid) {
        DateValid = dateValid;
    }
}
