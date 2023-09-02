package com.pnc.marketplace.implementation.communication;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.database.communication.MessageRepository;
import com.pnc.marketplace.model.communication.Message;
import com.pnc.marketplace.service.communication.MessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageServiceImp implements MessageService {

    @Autowired
    private MessageRepository messageRepo;

    @Override
    public Message saveMessages(Message message) {
        log.info("Saving new message in database");
        return this.messageRepo.save(message);
    }

    @Override
    public List<Message> getAllMessageByChat(String userId, String RecevierId) {

        log.info("Getting all messages of chat");

        List<Message> chats = this.messageRepo.findAllBySenderEmailAndRecevierEmailOrRecevierEmailAndSenderEmail(userId,
                RecevierId, userId, RecevierId);
        return chats;
    }

    @Override
    public Boolean readOneMessage(Message message) {

        log.info("Reading one message");

        Message response = this.messageRepo.findById(message.getId()).get();

        if (response == null)
            return false;

        response.setRead(true);

        this.messageRepo.save(response);

        return true;
    }

    @Override
    public Boolean readAllMessages(String from, String to) {

        log.info("Reading all messages");
        
        List<Message> chats = this.messageRepo.findAllBySenderEmailAndRecevierEmail(from, to);

        if (chats == null)
            return false;

        chats = chats.stream()
                .peek(chat -> chat.setRead(true))
                .collect(Collectors.toList());

        this.messageRepo.saveAll(chats);

        return true;
    }

}
