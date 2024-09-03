package com.ang.foro.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ang.foro.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Boolean existsByUsername (String username);

    @Query("SELECT new com.ang.foro.repository.UserPOJO(u) FROM User u")
    Page<Object[]> findAllUsers (Pageable pageable);
}