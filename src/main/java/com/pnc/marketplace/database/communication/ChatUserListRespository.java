package com.pnc.marketplace.database.communication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.communication.ChatUserList;

public interface ChatUserListRespository  extends JpaRepository<ChatUserList,Long>{
    
    List<ChatUserList> findBySendByEmail(String email);

    boolean existsBySendByEmailAndSendToEmailOrSendByEmailAndSendToEmail(
            String sendByEmail, String sendToEmail,
            String sendByEmail1, String sendToEmail1);

}
