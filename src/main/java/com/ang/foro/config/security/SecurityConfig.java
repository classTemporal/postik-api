package com.ang.foro.config.security;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Value("${secrets.keys}")
    private String secretKey;
    private SecretKey key;

    @PostConstruct
    public void postConstruct() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/post/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTAuthorizationFilter(key), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // @Bean
    // public static PropertySourcesPlaceholderConfigurer
    // propertyPlaceholderConfigurer() {
    // return new PropertySourcesPlaceholderConfigurer();
    // }
}