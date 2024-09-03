package com.ang.foro.repository;

import java.util.List;

import com.ang.foro.model.Post;
import com.ang.foro.model.User;

import lombok.Getter;

@Getter
public class UserPOJO {
    private Long id;
    private String username;
    private String role;
    private List<Post> posts;

    public UserPOJO(User user) {
        this.id = user.getId();
        this.posts = user.getPosts();
        this.username = user.getUsername();
        this.role = user.getRole().getName();
    }
}