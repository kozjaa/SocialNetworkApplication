package com.example.market.controller;

import com.example.market.model.User;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FriendsController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/home/addfriend/{id}")
    public String addFriend(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        userService.sendFriendsRequest(user);
        return "redirect:/home";
    }

    @RequestMapping(value = "/home/addfriend/accept/{username}")
    public String acceptFriend(@PathVariable String username) {
        User user = userService.getUserByName(username);
        userService.addMyFriend(user);
        User current = userService.getCurrentLoggedUser();
        current.setInvitations(current.getInvitations()-1);
        userService.updateUser(current);

        return "redirect:/myfriends";
    }

    @RequestMapping(value = "/home/addfriend/reject/{username}")
    public String rejectFriend(@PathVariable String username) {
        User user = userService.getUserByName(username);
        userService.rejectRequest(user);
        User current = userService.getCurrentLoggedUser();
        current.setInvitations(current.getInvitations()-1);
        userService.updateUser(current);

        return "redirect:/myfriends";
    }

    @RequestMapping(value = "/home/deletefriend/{id}")
    public String deleteFriend(@PathVariable Integer id) {
        userService.deletMyFriend(id);
        return "redirect:/myfriends";
    }

    @RequestMapping(value = "/myfriends")
    public String getFriendsByUserId(Model model) {
        model.addAttribute("friends", userService.getMyFriends());
        model.addAttribute("username", userService.getCurrentLoggedUser().getUsername());
        model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
        model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
        return "myprofile";
    }
}
