package com.pnc.marketplace.model.stripe;

public class SubscriptionPayment {
    
    private String email;
    private String type;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
}
