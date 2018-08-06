package com.example.market.controller;

import com.example.market.model.Post;
import com.example.market.model.User;
import com.example.market.service.PostService;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @RequestMapping(value = "/myprofile")
    public String getMyProfile(Model model) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        User currentUser = userService.getCurrentLoggedUser();
        List<User> myFriends = userService.getMyFriends();
        List<Post> myPosts = postService.getMyOwnPosts();
        Integer myNumberOfFriendsInvitations = currentUser.getInvitations();
        Integer myNumberOfNewMessages = currentUser.getNewmessage();
        Integer myNumberOfNotifications = currentUser.getNotification();

        model.addAttribute("user", currentUser);
        model.addAttribute("friends", myFriends);
        model.addAttribute("username", currentUsername);
        model.addAttribute("invitations", myNumberOfFriendsInvitations);
        model.addAttribute("newmessage", myNumberOfNewMessages);
        model.addAttribute("posts", myPosts);
        model.addAttribute("notifications", myNumberOfNotifications);
        return "myprofile";
    }

    @RequestMapping(value = "/user/{id}")
    public String getUserById(@PathVariable Integer id, Model model) {
        User userById = userService.getUserById(id);
        String userByIdUsername = userById.getUsername();
        User currentUser = userService.getCurrentLoggedUser();
        String currentUsername = currentUser.getUsername();
        List<User> myFriends = userService.getMyFriends();
        List<User> userByIdFriends = userById.getBefriended();
        List<Post> userByIdPosts = userById.getMyposts();
        List<String> myFriendsRequestsUsernames = currentUser.getRequestFriendsUsername();
        List<String> userByIdFriendsRequestsUsernames = userById.getRequestFriendsUsername();
        Integer myNumberOfFriendsInvitations = currentUser.getInvitations();
        Integer myNumberOfNewMessages = currentUser.getNewmessage();
        Integer myNumberOfNotifications = currentUser.getNotification();
        boolean isRequestSent = userByIdFriendsRequestsUsernames.contains(currentUsername);
        boolean isRequestGiven = myFriendsRequestsUsernames.contains(userByIdUsername);
        boolean isFriend = myFriends.contains(userById);

        if (userByIdUsername.equals(currentUsername)) {
            return "redirect:/myprofile";}
        else {
            model.addAttribute("friend", isFriend);
            model.addAttribute("request", isRequestGiven);
            model.addAttribute("is", isRequestSent);
            model.addAttribute("user", userById);
            model.addAttribute("username", currentUsername);
            model.addAttribute("invitations", myNumberOfFriendsInvitations);
            model.addAttribute("newmessage", myNumberOfNewMessages);
            model.addAttribute("friends", userByIdFriends);
            model.addAttribute("posts", userByIdPosts);
            model.addAttribute("notifications", myNumberOfNotifications);
            return "user";}
    }

    @RequestMapping(value = "/user/byname/{name}")
    public String getUserByName(@PathVariable String name) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();

        if (name.equals(currentUsername)) {
            return "redirect:/myprofile"; }
        else {
            Integer byNameUserId = userService.getUserByName(name).getId();
            return "redirect:/user/" + byNameUserId;}
    }
}
