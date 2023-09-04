package com.pnc.marketplace.controller.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.pnc.marketplace.model.communication.Message;
import com.pnc.marketplace.service.communication.MessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    /**
     * The receivedMessage function saves a message and returns it.
     * 
     * @param message The "message" parameter is of type Message and represents the message received
     * from the client.
     * @return The method is returning the received message.
     */
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    private Message receivedMessage(@Payload Message message) {

        this.messageService.saveMessages(message);

        return message;
    }

    /**
     * This function receives a private message, saves it, and sends it back to the sender.
     * 
     * @param message The "message" parameter is an object of type "Message" that is received as a
     * payload in the private message endpoint.
     * @return The method is returning the `Message` object that was received as a parameter.
     */
    @MessageMapping("/private-message")
    private Message receivedPrivateMessage(@Payload Message message) {

        try {
            log.info("Message: {}",message.toString());
            this.messageService.saveMessages(message);
            message.setType("RECEIVER");

            this.simpMessagingTemplate.convertAndSendToUser(message.getSenderEmail(), "/private", message);

        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
        }

        return message;
    }
}
