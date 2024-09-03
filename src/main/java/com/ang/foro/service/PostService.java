package com.ang.foro.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ang.foro.config.security.CurrentUser;
import com.ang.foro.model.Post;
import com.ang.foro.model.User;
import com.ang.foro.repository.PostRepository;
import com.ang.foro.repository.UserRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Page<Object[]> obtainAllPosts(@NonNull Pageable pageable) {
        return postRepository.findAllWithUser(pageable);
    }

    public Page<Post> obtainPostsByUsername(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found with username: "+ username);
        }
        return postRepository.findByUser(user, pageable);
    }

    public Page<Post> obtainUserPosts(@NonNull Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return postRepository.findByUser(user, pageable);
    }

    public Post createPost(@NonNull Long userId, String content, @CurrentUser String nameUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (user.getUsername().equals(nameUser)) {
            Post post = new Post();
            post.setUser(user);
            post.setContent(content);
            postRepository.save(post);

            return post;
        }
        return null;
    }

    public Post editPost(@NonNull Long postId, String newContent, @CurrentUser String nameUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

        if (post.getUser().getUsername().equals(nameUser)) {
            post.setContent(newContent);
            return post;
        }
        return null;
    }

    public Boolean deletePost(@NonNull Long postId, @CurrentUser String nameUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

        if (post.getUser().getUsername().equals(nameUser)) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }
}
