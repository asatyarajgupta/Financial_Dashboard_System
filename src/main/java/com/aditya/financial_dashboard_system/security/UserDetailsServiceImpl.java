package com.aditya.financial_dashboard_system.security;

import com.aditya.financial_dashboard_system.entities.userEntity;
import com.aditya.financial_dashboard_system.repos.userRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final userRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        userEntity user = repo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return User.withUsername(user.getUsername())
                .roles(user.getRole().name())
                .password(user.getPassword())
                .build();

    }
}
