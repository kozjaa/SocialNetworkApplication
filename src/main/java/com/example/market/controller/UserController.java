package com.example.market.controller;

import com.example.market.model.User;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/myprofile")
    public String getMyProfile(Model model) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        User currentUser = userService.getCurrentLoggedUser();
        model.addAttribute("user", userService.getCurrentLoggedUser());
        model.addAttribute("friends", userService.getMyFriends());
        model.addAttribute("username", currentUsername);
        model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
        model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
        return "myprofile";
    }

    @RequestMapping(value = "/user/{id}")
    public String getUserById(@PathVariable Integer id, Model model) {
        if (userService.getUserById(id).getUsername().equals(userService.getCurrentLoggedUser().getUsername())) {
            return "redirect:/myprofile";
        }
        else if (userService.getMyFriends().contains(userService.getUserById(id))) {
            model.addAttribute("user", userService.getUserById(id));
            model.addAttribute("username", userService.getCurrentLoggedUser().getUsername());
            model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
            model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
            return "friend";}
            else if (userService.getCurrentLoggedUser().getRequestFriendsUsername().contains(userService.getUserById(id).getUsername())) {
            model.addAttribute("user", userService.getUserById(id));
            model.addAttribute("username", userService.getCurrentLoggedUser().getUsername());
            model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
            model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
            return "requesteduser";
        }
        else {
            model.addAttribute("user", userService.getUserById(id));
            model.addAttribute("username", userService.getCurrentLoggedUser().getUsername());
            model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
            model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
        return "user";}
    }

    @RequestMapping(value = "/user/byname/{name}")
    public String getUserByName(@PathVariable String name) {
        if (name.equals(userService.getCurrentLoggedUser().getUsername())) {
            return "redirect:/myprofile";
        }
        else {
        Integer id = userService.getUserByName(name).getId();
        return "redirect:/user/" + id;}
    }
}
