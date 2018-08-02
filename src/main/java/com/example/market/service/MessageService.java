package com.example.market.service;

import com.example.market.model.Message;
import com.example.market.model.User;
import com.example.market.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveMessage(Message message) {
        String receiverName = message.getReceiver();
        User receiver = userService.getUserByName(receiverName);
        User current = userService.getCurrentLoggedUser();
        receiver.setNewmessage(receiver.getNewmessage()+1);
        message.setAuthor(current.getUsername());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTime.format(formatter);
        message.setDate(date);
        messageRepository.save(message);
        receiver.getMymessages().add(message);
        current.getMymessages().add(message);
    }
}
