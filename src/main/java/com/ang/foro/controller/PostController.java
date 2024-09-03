package com.ang.foro.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ang.foro.model.Post;
import com.ang.foro.repository.UserRepository;
import com.ang.foro.service.PostService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(origins = "${allowed.origins}")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    public PostController(PostService postService, UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @Transactional
    @GetMapping("/all")
    public ResponseEntity<Page<Object[]>> obtainAllPosts(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return ResponseEntity.ok(postService.obtainAllPosts(pageable));
    }

    @Transactional
    @GetMapping("/all/{username}")
    public ResponseEntity<Page<Post>> obtainPostsByUsername(@PathVariable(name = "username") String username,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(username))) {
            Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
            return ResponseEntity.ok(postService.obtainPostsByUsername(username, pageable));
        }
        return ResponseEntity.badRequest().build();
    }
}
