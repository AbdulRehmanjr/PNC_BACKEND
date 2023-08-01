package com.pnc.marketplace.model.join;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AppJoin {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int appJoinId;

    private int userJoin;

    private int sellerJoin;

    public int getAppJoinId() {
        return appJoinId;
    }

    public void setAppJoinId(int appJoinId) {
        this.appJoinId = appJoinId;
    }

    public int getUserJoin() {
        return userJoin;
    }

    public void setUserJoin(int userJoin) {
        this.userJoin = userJoin;
    }

    public int getSellerJoin() {
        return sellerJoin;
    }

    public void setSellerJoin(int sellerJoin) {
        this.sellerJoin = sellerJoin;
    }

    @Override
    public String toString() {
        return "AppJoin [appJoinId=" + appJoinId + ", userJoin=" + userJoin + ", sellerJoin=" + sellerJoin + "]";
    }
    

}
