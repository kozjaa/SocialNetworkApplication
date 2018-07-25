package com.example.market.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username")
    @NotBlank
    private String username;

    @Column(name = "password")
    @NotBlank
    private String password;

    @Transient
    private String repeatedPassword;

    private Integer newmessage = 0;

    private Integer invitations = 0;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "email")
    @NotBlank
    private String email;

    @ElementCollection
    private List<String> requestFriendsUsername;

    @OneToMany(mappedBy = "user")
    private List<Post> myposts;

    @JsonIgnore
    @JoinTable(
            name = "friendsmessages" , joinColumns = {@JoinColumn(name = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "id_message")}
    )
    @ManyToMany
    private List<Message> mymessages = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "befriended")
    private List<User> friends;

    @JsonIgnore
    @JoinTable(
            name = "friends", joinColumns = {@JoinColumn(name = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "id_friend")}
    )
    @ManyToMany
    private List<User> befriended;

    public User() {

    }

    public User(String username, String password , String email) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.enabled = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<User> getBefriended() {
        return befriended;
    }

    public void setBefriended(List<User> befriended) {
        this.befriended = befriended;
    }

    public List<Message> getMymessages() {
        return mymessages;
    }

    public void setMymessages(List<Message> mymessages) {
        this.mymessages = mymessages;
    }

    public List<String> getRequestFriendsUsername() {
        return requestFriendsUsername;
    }

    public void setRequestFriendsUsername(List<String> requestFriendsUsername) {
        this.requestFriendsUsername = requestFriendsUsername;
    }

    public List<Post> getMyposts() {
        return myposts;
    }

    public void setMyposts(List<Post> myposts) {
        this.myposts = myposts;
    }

    public Integer getNewmessage() {
        return newmessage;
    }

    public void setNewmessage(Integer newmessage) {
        this.newmessage = newmessage;
    }

    public Integer getInvitations() {
        return invitations;
    }

    public void setInvitations(Integer invitations) {
        this.invitations = invitations;
    }
}
