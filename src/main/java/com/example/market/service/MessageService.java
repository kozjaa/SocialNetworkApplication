package com.example.market.service;

import com.example.market.model.Message;
import com.example.market.model.User;
import com.example.market.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveMessage(Message message) {
        User user = userService.getUserByName(message.getReceiver());
        user.setNewmessage(user.getNewmessage()+1);
        message.setAuthor(userService.getCurrentLoggedUser().getUsername());
        messageRepository.save(message);
        String receiverName = message.getReceiver();
        User receiver = userService.getUserByName(receiverName);
        User current = userService.getCurrentLoggedUser();
        receiver.getMymessages().add(message);
        current.getMymessages().add(message);
    }
}
