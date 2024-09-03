package com.ang.foro.config.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import com.ang.foro.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {

    @Value("${secrets.keys}")
    private String secretKey;
    private SecretKey key;
    private final UserRepository userRepository;

    @PostConstruct
    public void postConstruct() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public JwtTokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JwtResponse generateToken(String username, String userId) {

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(userRepository.findByUsername(username).getRole().getName());

        String token = Jwts.builder().claims().subject(username).id(userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 600000)).and().claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .signWith(key).compact();
        return new JwtResponse(userId, username, "Bearer", token);
    }
}
