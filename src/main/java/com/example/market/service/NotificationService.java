package com.example.market.service;

import com.example.market.model.Notification;
import com.example.market.model.User;
import com.example.market.repository.NotificationRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserService userService;

    public List<Notification> getMyNotifications(){
        User user = userService.getCurrentLoggedUser();
        List<Notification> list = user.getNotifications();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        list.sort(Comparator.comparing(notification -> LocalDateTime.parse(notification.getDate(), formatter)));
        List<Notification> sortedList = Lists.reverse(list);
        return sortedList;
    }

    @Transactional
    public void saveNotification(String author, String post, String text, User receiver) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTime.format(formatter);
        Notification notification = new Notification();
        notification.setAuthor(author);
        notification.setPost(post);
        notification.setUser(receiver);
        notification.setContent(text);
        notification.setDate(date);
        notificationRepository.save(notification);
        receiver.getNotifications().add(notification);
        receiver.setNotification(receiver.getNotification()+1);
        userService.updateUser(receiver);
    }
}
