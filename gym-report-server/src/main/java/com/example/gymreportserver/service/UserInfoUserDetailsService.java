package com.example.gymreportserver.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserInfoUserDetailsService implements UserDetailsService {
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        String username = jwtService.extractUsername(token);
        String role = "ROLE_" + jwtService.extractAllClaims(token).get("roles").toString();

        if (username != null) {
            return User.withUsername(username)
                    .authorities(role)
                    .password("")
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid token");
        }
    }
}