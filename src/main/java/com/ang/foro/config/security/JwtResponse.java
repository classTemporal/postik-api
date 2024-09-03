package com.ang.foro.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String id;
    private String username;
    private String tokenType;
    private String accessToken;
}
