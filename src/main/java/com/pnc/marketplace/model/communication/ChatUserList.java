package com.pnc.marketplace.model.communication;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Getter
@Setter
@Entity
public class ChatUserList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatUserListId;

    private String sendByEmail;

    private String sendByName;

    private String sendByPic;

    private String sendToEmail;

    private String sendToName;

    private String sendToPic;

}
