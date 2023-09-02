package com.pnc.marketplace.service.communication;

import java.util.List;

import com.pnc.marketplace.model.communication.ChatUserList;

public interface ChatUserListService {
    
    /**
     * The function creates a new ChatUserList object.
     * 
     * @param chatUserList The chatUserList parameter is an object of type ChatUserList, which
     * represents a list of chat users.
     * @return The function createCUL is returning a ChatUserList object.
     */
    ChatUserList createCUL(ChatUserList chatUserList);

    /**
     * The function adds a user to a list of chat users.
     * 
     * @param List The List parameter is a ChatUserList object, which represents a list of chat users.
     * @return a new ChatUserList object that includes the added user.
     */
    ChatUserList addUser(ChatUserList List);

    /**
     * The function "getChatListBySender" returns a list of chat users based on the sender's email.
     * 
     * @param senderEmail The email address of the sender whose chat list needs to be retrieved.
     * @return The method is returning a list of ChatUserList objects.
     */
    List<ChatUserList> getChatListBySender(String senderEmail);
}
