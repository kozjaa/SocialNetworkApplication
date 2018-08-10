package com.example.market.controller;

import com.example.market.model.User;
import com.example.market.service.FriendService;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FriendsController {
    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;

    @RequestMapping(value = "/home/addfriend/{id}")
    public String addFriend(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        friendService.sendFriendsRequest(user);
        return "redirect:/user/" + id;
    }

    @RequestMapping(value = "/home/addfriend/accept/{username}")
    public String acceptFriend(@PathVariable String username) {
        friendService.acceptFriendsRequest(username);
        return "redirect:/invitations";
    }

    @RequestMapping(value = "/home/addfriend/reject/{username}")
    public String rejectFriend(@PathVariable String username) {
        friendService.rejectFriendsRequest(username);
        return "redirect:/invitations";
    }

    @RequestMapping(value = "/home/deletefriend/{id}")
    public String deleteFriend(@PathVariable Integer id) {
        friendService.deletMyFriend(id);
        return "redirect:/user/" + id;
    }
}
