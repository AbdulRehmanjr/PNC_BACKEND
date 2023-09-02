package com.pnc.marketplace.database.communication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pnc.marketplace.model.communication.Message;

public interface MessageRepository extends JpaRepository<Message,Long>{


    List<Message> findAllBySenderEmailAndRecevierEmailOrRecevierEmailAndSenderEmail(String userId, String receiverId, String receiverId2, String userId2);

    List<Message> findAllBySenderEmailAndRecevierEmail(String userId, String receiverId);

    List<Message> findByRecevierEmailAndSenderEmailAndIsRead(String receiverEmail, String senderEmail, boolean isRead);


}
