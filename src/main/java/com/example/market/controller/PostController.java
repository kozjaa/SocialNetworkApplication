package com.example.market.controller;

import com.example.market.model.User;
import com.example.market.service.PostService;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/post/like/{id}")
    public String likePost(@PathVariable Integer id){
        User currentUser = userService.getCurrentLoggedUser();
        if (currentUser.getLikedPosts().contains(id)){
            postService.changeLikedPost(id);
            return "redirect:/home"; }
        else if (currentUser.getUnlikedPosts().contains(id)){
            return "redirect:/home"; }
        else {
            postService.likePost(id);
        return "redirect:/home";}
    }

    @RequestMapping(value = "/post/unlike/{id}")
    public String unikePost(@PathVariable Integer id){
        User currentUser = userService.getCurrentLoggedUser();
        if (currentUser.getUnlikedPosts().contains(id)){
            postService.changeUnlikedPost(id);
            return "redirect:/home";}
        else if (currentUser.getLikedPosts().contains(id)){
            return "redirect:/home"; }
        else {
            postService.unlikePost(id);
            return "redirect:/home";}
    }

    @RequestMapping(value = "/user/post/like/{id}")
    public String likeUserPost(@PathVariable Integer id){
        User currentUser = userService.getCurrentLoggedUser();
        User receiver = userService.getUserByName(postService.getPostById(id).getAuthor());
        Integer receiverId = receiver.getId();
        if (currentUser.getLikedPosts().contains(id)){
            postService.changeLikedPost(id);
            return "redirect:/user/" + receiverId;
        }
        else if (currentUser.getUnlikedPosts().contains(id)){
            return "redirect:/user/" + receiverId;
        }
        else {
            postService.likePost(id);
            return "redirect:/user/" + receiverId;}
    }

    @RequestMapping(value = "/user/post/unlike/{id}")
    public String unikeUserPost(@PathVariable Integer id){
        User currentUser = userService.getCurrentLoggedUser();
        User receiver = userService.getUserByName(postService.getPostById(id).getAuthor());
        Integer receiverId = receiver.getId();
        if (currentUser.getUnlikedPosts().contains(id)){
            postService.changeUnlikedPost(id);
            return "redirect:/user/" + receiverId;}
        else if (currentUser.getLikedPosts().contains(id)){
            return "redirect:/user/" + receiverId;
        }
        else {
            postService.unlikePost(id);
            return "redirect:/user/" + receiverId;}
    }
}
