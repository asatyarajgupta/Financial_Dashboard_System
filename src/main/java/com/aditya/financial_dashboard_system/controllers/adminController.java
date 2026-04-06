package com.aditya.financial_dashboard_system.controllers;

import com.aditya.financial_dashboard_system.entities.adminEntity;
import com.aditya.financial_dashboard_system.entities.userEntity;
import com.aditya.financial_dashboard_system.services.userService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class adminController {
    private final userService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {

        return service.getUserById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody adminEntity user) {
        userEntity registeringUser = new userEntity();
        registeringUser.setUsername(user.getUsername());
        registeringUser.setRole(user.getRole());
        registeringUser.setPassword(user.getPassword());
        registeringUser.setEmail(user.getEmail());
        return service.saveUser(registeringUser, registeringUser.getRole());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody userEntity user) {
        return service.updateUser(id,user);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteUsers() {
        return service.delete();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteUsers(@PathVariable UUID id) {
        return service.deleteById(id);
    }


}
