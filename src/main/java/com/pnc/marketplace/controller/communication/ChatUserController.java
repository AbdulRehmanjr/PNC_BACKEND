package com.pnc.marketplace.controller.communication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pnc.marketplace.model.communication.ChatUserList;
import com.pnc.marketplace.service.communication.ChatUserListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/chatlist")
public class ChatUserController {
    
    @Autowired
    private ChatUserListService culService;

    /**
     * This function retrieves a chat list based on the sender's email address.
     * 
     * @param senderEmail The senderEmail parameter is a String that represents the email address of
     * the sender.
     * @return The method is returning a ResponseEntity object.
     */
    @GetMapping("/{senderEmail}")
    ResponseEntity<?> getChatListBySenderId(@PathVariable String senderEmail){

        List<ChatUserList> response = this.culService.getChatListBySender(senderEmail);

        if(response!=null)
            return ResponseEntity.status(201).body(response);
        
        log.error("Chat List Empty");
        return ResponseEntity.status(201).body(null);
    }

    /**
     * The function is a POST endpoint that creates a chat user list and returns a response entity with
     * the created list or an error message.
     * 
     * @param chatUserList The parameter `chatUserList` is of type `ChatUserList`, which is a custom
     * class representing a list of chat users. It is annotated with `@RequestBody`, indicating that
     * the data for this parameter will be received in the request body.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/add")
    ResponseEntity<?> getChatListBySenderId(@RequestBody ChatUserList chatUserList){

        ChatUserList response = this.culService.createCUL(chatUserList);

        if(response!=null)
            return ResponseEntity.status(201).body(response);
        
        log.error("Chat List formation error");
        return ResponseEntity.status(201).body(null);
    }
}
