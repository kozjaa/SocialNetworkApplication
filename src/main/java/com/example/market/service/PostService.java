package com.example.market.service;

import com.example.market.model.Post;
import com.example.market.model.User;
import com.example.market.repository.PostRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private  UserService userService;

    public List<Post> getAllPosts() {
        List<User> myfriends = userService.getMyFriends();
        List<List<Post>> lists = myfriends.stream().map(user -> user.getMyposts()).collect(Collectors.toList());
        List<Post> friendsPosts = lists.stream().flatMap(List::stream).collect(Collectors.toList());
        friendsPosts.addAll(userService.getCurrentLoggedUser().getMyposts());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        friendsPosts.sort(Comparator.comparing(message -> LocalDateTime.parse(message.getDate(), formatter)));
        List<Post> sortPosts = Lists.reverse(friendsPosts);

        return sortPosts;
    }

    @Transactional
    public void savePost(Post post) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTime.format(formatter);
        post.setDate(date);
        post.setAuthor(username);
        post.setUser(userService.getCurrentLoggedUser());
        postRepository.save(post);
        userService.getCurrentLoggedUser().getMyposts().add(post);
    }
}
