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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

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

    @Autowired
    private UserRepositoryManager userRepositoryManager;

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
        userRepositoryManager.saveUser(user);
    }

    @Transactional
    public void addMyFriend(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.getUserByName(username);
        List<User> friends = currentUser.getBefriended();
        List<User> befriended = user.getBefriended();
        if (user.getUsername().equals(currentUser.getUsername()) || currentUser.getBefriended().contains(user) ||
                befriended.contains(currentUser)) {

        }
        else {
        friends.add(user);
        befriended.add(currentUser);
        currentUser.getRequestFriendsUsername().remove(user.getUsername());}
    }

    public List<User> getMyFriends() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.getUserByName(username);
        List<User> friends = user.getBefriended();

        return friends;
    }


    public List<Message> getAllMessages(String user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentuser = userRepository.getUserByName(username);
        List<Message> list = messageRepository.findAll();
        List<Message> newList = new ArrayList<>();
        for(Message o : list) {
            if((o.getReceiver().equals(username) || o.getAuthor().equals(username)) && (o.getReceiver().equals(user) || o.getAuthor().equals(user))) {

                newList.add(o);
            }
        }
        return newList;
    }

    @Transactional
    public void sendFriendsRequest(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.getUserByName(username);
        if (currentUser.getUsername().equals(user.getUsername()) || currentUser.getBefriended().contains(user)
                || user.getBefriended().contains(currentUser) || user.getRequestFriendsUsername().contains(username)) {

        }
        else {
        user.setInvitations(user.getInvitations()+1);
        user.getRequestFriendsUsername().add(username);}
    }

    public List<String> getAllFriendsRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.getUserByName(username);

        return user.getRequestFriendsUsername();
    }

    @Transactional
    public void rejectRequest(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.getUserByName(username);
        currentUser.getRequestFriendsUsername().remove(user.getUsername());
    }

    public Set<String> getConversations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.getUserByName(username);
        List<Message> messages = user.getMymessages();
        for(Message o : messages) {
            if(o.getReceiver().equals(username)) {

                o.setReceiver(o.getAuthor());
            }
        }
        List<String> conversations = messages.stream().map(message -> message.getReceiver()).collect(Collectors.toList());
        List<String> sortedconversations = Lists.reverse(conversations);
        Set<String> conv = new HashSet<>(sortedconversations);

        return conv;
    }

    @Transactional
    public void deletMyFriend(Integer id) {
        User current = getCurrentLoggedUser();
        User user = getUserById(id);
        List<User> myFriends = current.getBefriended();
        List<User> myFriendsuser = user.getBefriended();
        myFriends.remove(user);
        myFriendsuser.remove(current);
    }
}
