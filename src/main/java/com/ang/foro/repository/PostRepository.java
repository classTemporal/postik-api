package com.ang.foro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ang.foro.model.Post;
import com.ang.foro.model.User;


public interface PostRepository extends JpaRepository<Post, Long>{
    Page<Post> findByUser(User user, Pageable pageable);

    @Query("SELECT new com.ang.foro.repository.PostPOJO(p, u.username) FROM Post p JOIN p.user u")
    Page<Object[]> findAllWithUser(Pageable pageable);
}
