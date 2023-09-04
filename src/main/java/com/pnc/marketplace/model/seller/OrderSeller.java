package com.pnc.marketplace.model.seller;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderSeller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  orderSellerId;

    private String orderCode;

    private long sellerId;
}
