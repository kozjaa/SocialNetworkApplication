package com.example.market.controller;

import com.example.market.model.Post;
import com.example.market.model.User;
import com.example.market.service.NotificationService;
import com.example.market.service.PostService;
import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/post/like/{id}")
    public String likePost(@PathVariable Integer id){
        User currentUser = userService.getCurrentLoggedUser();
        User receiver = userService.getUserByName(postService.getPostById(id).getAuthor());
        if (currentUser.getLikedPosts().contains(id)){
            Post post = postService.getPostById(id);
            post.setLiked(post.getLiked()-1);
            postService.updatePost(post);
            List<Integer> likedPosts = currentUser.getLikedPosts();
            likedPosts.remove(id);
            currentUser.setLikedPosts(likedPosts);
            userService.updateUser(currentUser);;
            return "redirect:/home"; }
        else if (currentUser.getUnlikedPosts().contains(id)){
            return "redirect:/home"; }
        else {
            Post post = postService.getPostById(id);
            post.setLiked(post.getLiked()+1);
            postService.updatePost(post);
            List<Integer> likedPosts = currentUser.getLikedPosts();
            likedPosts.add(id);
            currentUser.setLikedPosts(likedPosts);
            userService.updateUser(currentUser);
            notificationService.saveLikeNotification(currentUser.getUsername(), post.getContent(), receiver);
        return "redirect:/home";}
    }

    @RequestMapping(value = "/post/unlike/{id}")
    public String unikePost(@PathVariable Integer id){
        User currentUser = userService.getCurrentLoggedUser();
        User receiver = userService.getUserByName(postService.getPostById(id).getAuthor());
        if (currentUser.getUnlikedPosts().contains(id)){
            Post post = postService.getPostById(id);
            post.setUnliked(post.getUnliked()-1);
            postService.updatePost(post);
            List<Integer> unlikedPosts = currentUser.getUnlikedPosts();
            unlikedPosts.remove(id);
            currentUser.setUnlikedPosts(unlikedPosts);
            userService.updateUser(currentUser);
            return "redirect:/home";}
        else if (currentUser.getLikedPosts().contains(id)){
            return "redirect:/home"; }
        else {
            Post post = postService.getPostById(id);
            post.setUnliked(post.getUnliked()+1);
            postService.updatePost(post);
            List<Integer> unlikedPosts = currentUser.getUnlikedPosts();
            unlikedPosts.add(id);
            currentUser.setUnlikedPosts(unlikedPosts);
            userService.updateUser(currentUser);
            notificationService.saveUnlikeNotification(currentUser.getUsername(), post.getContent(), receiver);
            return "redirect:/home";}
    }

    @RequestMapping(value = "/user/post/like/{id}")
    public String likeUserPost(@PathVariable Integer id){
        User currentUser = userService.getCurrentLoggedUser();
        User receiver = userService.getUserByName(postService.getPostById(id).getAuthor());
        Integer receiverId = receiver.getId();
        if (currentUser.getLikedPosts().contains(id)){
            Post post = postService.getPostById(id);
            post.setLiked(post.getLiked()-1);
            postService.updatePost(post);
            List<Integer> likedPosts = currentUser.getLikedPosts();
            likedPosts.remove(id);
            currentUser.setLikedPosts(likedPosts);
            userService.updateUser(currentUser);;
            return "redirect:/user/" + receiverId;
        }
        else if (currentUser.getUnlikedPosts().contains(id)){
            return "redirect:/user/" + receiverId;
        }
        else {
            Post post = postService.getPostById(id);
            post.setLiked(post.getLiked()+1);
            postService.updatePost(post);
            List<Integer> likedPosts = currentUser.getLikedPosts();
            likedPosts.add(id);
            currentUser.setLikedPosts(likedPosts);
            userService.updateUser(currentUser);
            notificationService.saveLikeNotification(currentUser.getUsername(), post.getContent(), receiver);
            return "redirect:/user/" + receiverId;}
    }

    @RequestMapping(value = "/user/post/unlike/{id}")
    public String unikeUserPost(@PathVariable Integer id){
        User currentUser = userService.getCurrentLoggedUser();
        User receiver = userService.getUserByName(postService.getPostById(id).getAuthor());
        Integer receiverId = receiver.getId();
        if (currentUser.getUnlikedPosts().contains(id)){
            Post post = postService.getPostById(id);
            post.setUnliked(post.getUnliked()-1);
            postService.updatePost(post);
            List<Integer> unlikedPosts = currentUser.getUnlikedPosts();
            unlikedPosts.remove(id);
            currentUser.setUnlikedPosts(unlikedPosts);
            userService.updateUser(currentUser);
            return "redirect:/user/" + receiverId;}
        else if (currentUser.getLikedPosts().contains(id)){
            return "redirect:/user/" + receiverId;
        }
        else {
            Post post = postService.getPostById(id);
            post.setUnliked(post.getUnliked()+1);
            postService.updatePost(post);
            List<Integer> unlikedPosts = currentUser.getUnlikedPosts();
            unlikedPosts.add(id);
            currentUser.setUnlikedPosts(unlikedPosts);
            userService.updateUser(currentUser);
            notificationService.saveUnlikeNotification(currentUser.getUsername(), post.getContent(), receiver);
            return "redirect:/user/" + receiverId;}
    }
}
