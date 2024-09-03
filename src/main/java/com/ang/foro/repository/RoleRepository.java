package com.ang.foro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ang.foro.model.Role;
import java.util.List;


public interface RoleRepository extends JpaRepository<Role, Long>{
    List<Role> findByName(String name);
}
