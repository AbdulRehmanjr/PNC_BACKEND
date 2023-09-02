
package com.pnc.marketplace.implementation.communication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pnc.marketplace.database.communication.ChatUserListRespository;
import com.pnc.marketplace.model.communication.ChatUserList;
import com.pnc.marketplace.service.communication.ChatUserListService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ChatUserListServiceImp implements ChatUserListService {


    @Autowired
    private ChatUserListRespository culRepo;

    @Override
    public ChatUserList createCUL(ChatUserList chatUserList) {

        /**
         * *check that sender to receiver and recevier to sender data does not present in databse 
         * ! if it is present do not make new one 
         * */

        boolean found = this.culRepo.existsBySendByEmailAndSendToEmailOrSendByEmailAndSendToEmail(chatUserList.getSendByEmail(), chatUserList.getSendToEmail(), chatUserList.getSendToEmail(),chatUserList.getSendToEmail());

        if(found == true)
            return null;

        ChatUserList recevier = new ChatUserList();

        recevier.setSendByEmail(chatUserList.getSendToEmail());
        recevier.setSendByName(chatUserList.getSendToName());
        recevier.setSendByPic(chatUserList.getSendToPic());
        recevier.setSendToEmail(chatUserList.getSendByEmail());
        recevier.setSendToName(chatUserList.getSendByName());
        recevier.setSendToPic(chatUserList.getSendByPic());

        ChatUserList response = this.culRepo.save(chatUserList);

        this.culRepo.save(recevier);
        
        if(response!=null)
            return response;

        log.error("Error in saving chat user list relation");
        return null;
    }

    @Override
    public ChatUserList addUser(ChatUserList List) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUser'");
    }

    @Override
    public List<ChatUserList> getChatListBySender(String senderEmail) {
        
        List<ChatUserList> response  = this.culRepo.findBySendByEmail(senderEmail);

        if(response!=null)
            return response;

        log.error("Chat list may be empty or null");
        return null;
    }
    
}
