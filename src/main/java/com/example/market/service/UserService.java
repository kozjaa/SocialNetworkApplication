package com.example.market.service;

import com.example.market.model.Role;
import com.example.market.model.Message;
import com.example.market.model.User;
import com.example.market.repository.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private MessageRepository messageRepository;

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public List<Object> getAllLogedUsers()
    {
        return sessionRegistry.getAllPrincipals();
    }

    public List<String> getAllUsersUsernames() {
        List<User> users =userRepository.findAll();
        List<String> allUsernames = users.stream().map(user1 -> user1.getUsername()).collect(Collectors.toList());
        return allUsernames;
    }

    public List<User> searchUsersByName(String username) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);
        users.sort(Comparator.comparing(user1 -> user1.getUsername()));
        return users;
    }

    public User getUserByName(String name)
    {
       return userRepository.getUserByName(name);
    }

    public User getUserById(Integer id)
    {
        return userRepository.getOne(id);
    }

    public User getCurrentLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.getUserByName(username);
    }

    @Transactional
    public void saveUser(User user) {
        Role role = new Role(user.getUsername(),"USER");
        roleRepository.save(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Transactional
    public void addMyFriend(User user) {
        User currentUser = getCurrentLoggedUser();
        List<User> friends = currentUser.getBefriended();
        List<User> befriended = user.getBefriended();

        if (user.getUsername().equals(currentUser.getUsername()) || currentUser.getBefriended().contains(user) ||
                befriended.contains(currentUser)) {}
        else {
        friends.add(user);
        befriended.add(currentUser);
        currentUser.getRequestFriendsUsername().remove(user.getUsername());}
    }

    public List<User> getMyFriends() {
        User currentUser = getCurrentLoggedUser();
        List<User> friends = currentUser.getBefriended();
        friends.sort(Comparator.comparing(user1 -> user1.getUsername()));
        return friends;
    }

    public List<Message> getAllMessages(String username) {
        String currentUsername = getCurrentLoggedUser().getUsername();
        List<Message> list = messageRepository.findAll();
        List<Message> newList = new ArrayList<>();

        for(Message o : list) {
            if((o.getReceiver().equals(currentUsername) || o.getAuthor().equals(currentUsername))
                    && (o.getReceiver().equals(username) || o.getAuthor().equals(username))) {
                newList.add(o); } }
        return newList;
    }

    @Transactional
    public void sendFriendsRequest(User user) {
        User currentUser = getCurrentLoggedUser();
        String currentUsername = currentUser.getUsername();

        if (currentUser.getUsername().equals(user.getUsername()) || currentUser.getBefriended().contains(user)
                || user.getBefriended().contains(currentUser) || user.getRequestFriendsUsername().contains(currentUsername)) {}
        else {
        user.setInvitations(user.getInvitations()+1);
        user.getRequestFriendsUsername().add(currentUsername);}
    }

    public List<String> getAllFriendsRequests() {
        User currentUser = getCurrentLoggedUser();
        List<String> friendsRequests = currentUser.getRequestFriendsUsername();
        return friendsRequests;
    }

    public List<String> getConversations() {
        User currentUser = getCurrentLoggedUser();
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

    @Transactional
    public void deletMyFriend(Integer id) {
        User current = getCurrentLoggedUser();
        User user = getUserById(id);
        List<User> myFriends = current.getBefriended();
        List<User> userFriends = user.getBefriended();
        myFriends.remove(user);
        userFriends.remove(current);
    }

    @Transactional
    public void acceptFriendsRequest(String username) {
        User friend = getUserByName(username);
        addMyFriend(friend);
        User current = getCurrentLoggedUser();
        current.setInvitations(current.getInvitations()-1);
        updateUser(current);
    }

    @Transactional
    public void rejectFriendsRequest(String username) {
        User current = getCurrentLoggedUser();
        current.setInvitations(current.getInvitations()-1);
        updateUser(current);
        current.getRequestFriendsUsername().remove(username);
    }
}
