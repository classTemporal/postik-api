package com.ang.foro.repository;

import com.ang.foro.model.Post;

import lombok.Getter;

@Getter
public class PostPOJO {
    private Long id;
    private String username;
    private String content;

    public PostPOJO(Post post, String username) {
        this.id = post.getId();
        this.content = post.getContent();
        this.username = username;
    }
}