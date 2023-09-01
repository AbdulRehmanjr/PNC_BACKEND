package com.pnc.marketplace.model.communication;

import java.util.List;

import com.pnc.marketplace.model.User;
import com.pnc.marketplace.model.seller.Seller;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ChatUserList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatUserList;

    @OneToOne
    private Seller seller;

    @OneToMany
    private List<User> users;

}
