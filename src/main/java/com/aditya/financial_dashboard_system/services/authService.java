package com.aditya.financial_dashboard_system.services;

import com.aditya.financial_dashboard_system.entities.registerEntity;
import com.aditya.financial_dashboard_system.exceptions.noSuchEntityExists;
import com.aditya.financial_dashboard_system.security.UserDetailsServiceImpl;
import com.aditya.financial_dashboard_system.utils.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class authService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final jwtUtil util;

    public ResponseEntity<?> loginService(registerEntity user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            return new ResponseEntity<>(util.generateToken(userDetails.getUsername()), HttpStatus.OK);
        } catch (Exception e) {
            throw new noSuchEntityExists("No Such User Exists");
        }
    }
}