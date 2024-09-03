package com.ang.foro.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ang.foro.config.security.CurrentUser;
import com.ang.foro.model.Post;
import com.ang.foro.model.User;
import com.ang.foro.repository.UserRepository;
import com.ang.foro.service.PostService;
import com.ang.foro.service.UserService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "${allowed.origins}")
public class UserController {
    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(PostService postService, UserRepository userRepository, UserService userService) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    @GetMapping("/all")
    public ResponseEntity<Page<Object[]>> allUsers(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("username"));
        return ResponseEntity.ok(userService.allUsers(pageable));
    }

    @Transactional
    @GetMapping("/{userId}")
    public ResponseEntity<Page<Post>> obtainPostsByUserId(@PathVariable(name = "userId") @NonNull Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
            return ResponseEntity.ok(postService.obtainUserPosts(userId, pageable));
        }
        return ResponseEntity.badRequest().build();
    }

    @Transactional
    @PostMapping("/{userId}/create")
    public ResponseEntity<Post> createPost(@PathVariable(name = "userId") @NonNull Long userId,
            @RequestBody PostRequest postRequest, @CurrentUser String nameUser) {
        Post newPost = postService.createPost(userId, postRequest.getContent(), nameUser);
        if (newPost != null) {
            return ResponseEntity.ok(newPost);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    @PutMapping("/{userId}/edit/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable(name = "userId") @NonNull Long userId,
            @PathVariable(name = "postId") @NonNull Long postId, @RequestBody PostRequest postRequest,
            @CurrentUser String nameUser) {
        Post postEdited = postService.editPost(postId, postRequest.getContent(), nameUser);
        if (postEdited != null) {
            return ResponseEntity.ok(postEdited);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    @DeleteMapping("/{userId}/delete/{postId}")
    public ResponseEntity<Void> eliminarPost(@PathVariable(name = "userId") @NonNull Long userId,
            @PathVariable(name = "postId") @NonNull Long postId, @CurrentUser String nameUser) {
        if (Boolean.TRUE.equals(postService.deletePost(postId, nameUser))) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
