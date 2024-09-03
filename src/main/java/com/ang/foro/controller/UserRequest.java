package com.ang.foro.controller;

public record UserRequest(String username, String password) {

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
