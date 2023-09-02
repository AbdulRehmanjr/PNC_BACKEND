package com.pnc.marketplace.controller.communication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnc.marketplace.model.communication.Message;
import com.pnc.marketplace.service.communication.MessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // @GetMapping("/count/{userId}")
    // ResponseEntity<?> getMessageCount(@PathVariable String userId) {

    // log.info("Getting chat list by userId");

    // int count = this.culService.getCountMessages(userId);

    // return ResponseEntity.status(201).body(count);
    // }

    /**
     * The function retrieves all chat messages between two users based on their user IDs.
     * 
     * @param userId The userId parameter represents the ID of the user who is requesting the chat
     * messages.
     * @param receiverId The receiverId parameter is the ID of the user who is receiving the chat
     * messages.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{userId}/{receiverId}")
    ResponseEntity<?> getAllChatByRoom(@PathVariable String userId, @PathVariable String receiverId) {

        log.info("Getting all chats of room");

        List<Message> chats = this.messageService.getAllMessageByChat(userId, receiverId);

        if (chats == null)
            return ResponseEntity.status(404).body(null);

        return ResponseEntity.status(201).body(chats);
    }

    /**
     * The function reads all messages between a user and a receiver and returns a response indicating
     * whether the operation was successful or not.
     * 
     * @param userId The userId parameter represents the ID of the user who wants to read the messages.
     * @param receiverId The receiverId parameter is the ID of the user who will receive the messages.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/read-messages/{userId}/{receiverId}")
    ResponseEntity<?> readAllMessages(@PathVariable String userId, @PathVariable String receiverId) {

        Boolean response = this.messageService.readAllMessages(userId, receiverId);

        if (response == false)
            return ResponseEntity.status(404).body(null);

        return ResponseEntity.status(201).body(response);
    }

    /**
     * The function "readOneMessage" reads a single message and returns a response indicating whether
     * the operation was successful or not.
     * 
     * @param message The parameter "message" is of type Message and it is annotated with @RequestBody.
     * This means that the method expects a JSON object in the request body and it will be
     * automatically converted to a Message object.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/read-message")
    ResponseEntity<?> readOneMessage(@RequestBody Message message) {

        Boolean response = this.messageService.readOneMessage(message);

        if (response == false)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);

        return ResponseEntity.status(201).body(response);
    }
}
