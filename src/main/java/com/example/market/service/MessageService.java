package com.example.market.service;

import com.example.market.model.Message;
import com.example.market.model.User;
import com.example.market.repository.MessageRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getConversations() {
        User currentUser = userService.getCurrentLoggedUser();
        String currentUsername = currentUser.getUsername();
        List<Message> messages = currentUser.getMymessages();

        for(Message o : messages) {
            if(o.getReceiver().equals(currentUsername)) {
                o.setReceiver(o.getAuthor()); } }

        List<String> conversations = messages.stream().map(message -> message.getReceiver()).collect(Collectors.toList());
        List<String> sortedconversations = Lists.reverse(conversations);
        List<String> list = sortedconversations.stream().distinct().collect(Collectors.toList());
        return list;
    }

    public List<Message> getAllMessages(String username) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        List<Message> list = messageRepository.findAll();
        List<Message> newList = new ArrayList<>();

        for(Message o : list) {
            if((o.getReceiver().equals(currentUsername) || o.getAuthor().equals(currentUsername))
                    && (o.getReceiver().equals(username) || o.getAuthor().equals(username))) {
                newList.add(o); } }
        return newList;
    }
}
