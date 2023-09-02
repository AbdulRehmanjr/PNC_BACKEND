package com.pnc.marketplace.service.communication;

import java.util.List;

import com.pnc.marketplace.model.communication.Message;

public interface MessageService {
    
    /**
     * The function saves a message and returns the saved message.
     * 
     * @param message The "message" parameter is an object of type "Message".
     * @return The function saveMessages is returning a Message object.
     */
    Message saveMessages(Message message);

    /**
     * The function retrieves all messages in a chat between a user and a receiver.
     * 
     * @param userId The ID of the user who sent the messages.
     * @param receiverId The ID of the user who is receiving the messages.
     * @return The method is returning a list of messages.
     */
    List<Message> getAllMessageByChat(String userId,String receiverId);

    /**
     * The function "readOneMessage" reads a single message and returns a boolean value indicating
     * whether the message was successfully read.
     * 
     * @param message The message parameter is of type Message, which means it represents a message
     * object.
     * @return A boolean value is being returned.
     */
    Boolean readOneMessage(Message message);

    /**
     * The function readAllMessages takes two parameters, "from" and "to", and returns a Boolean value
     * indicating whether all messages between the two specified users have been read.
     * 
     * @param from The sender of the messages.
     * @param to The recipient of the messages.
     * @return The method readAllMessages returns a boolean value.
     */
    Boolean readAllMessages(String from,String to);
}
