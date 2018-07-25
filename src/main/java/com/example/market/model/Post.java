package com.example.market.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "posts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "author")
    private String author;

    @Column(name = "content")
    @NotBlank
    private String content;

    @Column(name = "date")
    private String date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
