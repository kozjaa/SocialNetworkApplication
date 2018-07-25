package com.example.market.controller;


import com.example.market.model.Post;
import com.example.market.model.User;
import com.example.market.service.PostService;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register")
    public String registerUser(Model model) {
        model.addAttribute("user", new User());
        return "registerform";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveUser(@Valid User user, BindingResult bindingResult) {
        List<String> allUsernames = userService.getAllUsersUsernames();
        String username = user.getUsername();
        String password = user.getPassword();
        String repeatedPassword = user.getRepeatedPassword();

        if (bindingResult.hasErrors() || allUsernames.contains(username) || !password.equals(repeatedPassword)) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getObjectName() + " " + error.getDefaultMessage());
            });
            return "registerform";
        }
        else {
            userService.saveUser(user);
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/home")
    public String home(Model model) {
        User currentUser = userService.getCurrentLoggedUser();
        List<Post> allMesages = postService.getAllPosts();
        Integer currentUserId = currentUser.getId();
        String currentUsername = currentUser.getUsername();
        List<String> requestFriendsUsers = userService.getAllFriendsRequests();

        model.addAttribute("invitations", currentUser.getInvitations());
        model.addAttribute("newmessage", currentUser.getNewmessage());
        model.addAttribute("id", currentUserId);
        model.addAttribute("posts", allMesages);
        model.addAttribute("post", new Post());
        model.addAttribute("username", currentUsername);
        model.addAttribute("users", requestFriendsUsers);

        return "index";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String addMessage(@Valid Post post, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
        {
            bindingResult.getAllErrors().forEach(error ->{
                System.out.println(error.getObjectName() + " " + error.getDefaultMessage());
            });
            return "redirect:/home";}
            else {
        postService.savePost(post);
        return "redirect:/home";}
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, @RequestParam(value = "search") String username) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        if(username.trim().equals("")) {
            model.addAttribute("result", "Nie podano żadnych danych do wyszukania");
            model.addAttribute("username", currentUsername);
            model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
            model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
            return "nouser";
        }
        else if (!userService.getAllUsers().contains(userService.getUserByName(username.trim()))){
            model.addAttribute("user", username);
            model.addAttribute("username", currentUsername);
            model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
            model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());

            return "nouser";
        }
        else {
            User user = userService.getUserByName(username.trim());
            model.addAttribute("user", user);
            model.addAttribute("username", currentUsername);
            model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
            model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
            return "searchresult";}
    }

    @RequestMapping(value = "/invitations")
    public String getInvitations(Model model) {
        model.addAttribute("users", userService.getAllFriendsRequests());
        model.addAttribute("username", userService.getCurrentLoggedUser().getUsername());
        model.addAttribute("invitations", userService.getCurrentLoggedUser().getInvitations());
        model.addAttribute("newmessage", userService.getCurrentLoggedUser().getNewmessage());
        return "invitations";
    }

}
