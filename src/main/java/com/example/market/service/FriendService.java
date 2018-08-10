package com.example.market.service;

import com.example.market.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class FriendService {
    @Autowired
    private UserService userService;

    @Transactional
    public void addMyFriend(User user) {
        User currentUser = userService.getCurrentLoggedUser();
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
        User currentUser = userService.getCurrentLoggedUser();
        List<User> friends = currentUser.getBefriended();
        friends.sort(Comparator.comparing(user1 -> user1.getUsername()));
        return friends;
    }

    @Transactional
    public void sendFriendsRequest(User user) {
        User currentUser = userService.getCurrentLoggedUser();
        String currentUsername = currentUser.getUsername();

        if (currentUser.getUsername().equals(user.getUsername()) || currentUser.getBefriended().contains(user)
                || user.getBefriended().contains(currentUser) || user.getRequestFriendsUsername().contains(currentUsername)) {}
        else {
            user.setInvitations(user.getInvitations()+1);
            user.getRequestFriendsUsername().add(currentUsername);}
    }

    public List<String> getAllFriendsRequests() {
        User currentUser = userService.getCurrentLoggedUser();
        List<String> friendsRequests = currentUser.getRequestFriendsUsername();
        return friendsRequests;
    }

    @Transactional
    public void deletMyFriend(Integer id) {
        User current = userService.getCurrentLoggedUser();
        User user = userService.getUserById(id);
        List<User> myFriends = current.getBefriended();
        List<User> userFriends = user.getBefriended();
        myFriends.remove(user);
        userFriends.remove(current);
    }

    @Transactional
    public void acceptFriendsRequest(String username) {
        User friend = userService.getUserByName(username);
        addMyFriend(friend);
        User current = userService.getCurrentLoggedUser();
        current.setInvitations(current.getInvitations()-1);
        userService.updateUser(current);
    }

    @Transactional
    public void rejectFriendsRequest(String username) {
        User current = userService.getCurrentLoggedUser();
        current.setInvitations(current.getInvitations()-1);
        userService.updateUser(current);
        current.getRequestFriendsUsername().remove(username);
    }
}
