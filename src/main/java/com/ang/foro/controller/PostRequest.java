package com.ang.foro.controller;

public record PostRequest(String content) {

    public String getContent() {
        return content;
    }
}
