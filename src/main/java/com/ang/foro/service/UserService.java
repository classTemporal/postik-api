package com.ang.foro.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ang.foro.config.security.JwtResponse;
import com.ang.foro.config.security.JwtTokenProvider;
import com.ang.foro.model.Role;
import com.ang.foro.model.User;
import com.ang.foro.repository.RoleRepository;
import com.ang.foro.repository.UserRepository;

import jakarta.validation.constraints.NotBlank;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Page<Object[]> allUsers(Pageable pageable) {
        return userRepository.findAllUsers(pageable);
    }

    public JwtResponse createNewUser(@NotBlank String username, @NotBlank String password) {
        Role role = roleRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("User Role Not Found"));

        if (Boolean.FALSE.equals(userRepository.existsByUsername(username))) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRole(role);
            userRepository.save(newUser);
            return jwtTokenProvider.generateToken(username, newUser.getId().toString());
        }
        return null;
    }

    public JwtResponse loginExistsUser(@NotBlank String username, @NotBlank String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtTokenProvider.generateToken(username, user.getId().toString());
        }
        return null;
    }
}
