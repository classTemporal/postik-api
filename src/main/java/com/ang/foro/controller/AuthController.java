package com.ang.foro.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ang.foro.config.security.JwtResponse;
import com.ang.foro.config.security.MessageResponse;
import com.ang.foro.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${allowed.origins}")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserRequest userRequest) {
        if (userRequest.getUsername() != null && userRequest.getPassword() != null) {
            JwtResponse userToken = userService.createNewUser(userRequest.getUsername(), userRequest.getPassword());
            if (userToken != null) {
                return ResponseEntity.ok(userToken);
            }
            return ResponseEntity.badRequest().body(new MessageResponse("User already taken."));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Wrong username or password."));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserRequest userRequest) {
        JwtResponse userToken = userService.loginExistsUser(userRequest.getUsername(), userRequest.getPassword());
        if (userToken != null) {
            return ResponseEntity.ok(userToken);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Username not found or wrong password."));
    }
}