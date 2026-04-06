package com.aditya.financial_dashboard_system.services;

import com.aditya.financial_dashboard_system.entities.registerEntity;
import com.aditya.financial_dashboard_system.entities.userEntity;
import com.aditya.financial_dashboard_system.entities.validEntity;
import com.aditya.financial_dashboard_system.exceptions.noSuchEntityExists;
import com.aditya.financial_dashboard_system.repos.userRepo;
import com.aditya.financial_dashboard_system.utils.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class userService {
    private final userRepo repo;
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok((List<userEntity>) repo.findAll());
    }

    public ResponseEntity<?> getUserById(UUID id) {
        Optional<userEntity> user = repo.findById(id);
        if (user.isEmpty()) {
            throw new noSuchEntityExists("No Such User Exists");
        }
        return ResponseEntity.ok(repo.findById(id));
    }

    public ResponseEntity<?> saveUser(userEntity user, Role role){
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is invalid");
        }
        if (user.getEmail() == null  || user.getEmail() == "") {
            throw new IllegalArgumentException("Email is invalid");
        }
        if (user.getPassword() == null  || user.getPassword() == "") {
            throw new IllegalArgumentException("Password is invalid");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(role);
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    public ResponseEntity<?> updateUser(UUID id,userEntity user) {
        Optional<userEntity> updatedUser = repo.findById(id);
        if (updatedUser.isEmpty()) {
            throw new noSuchEntityExists("No Such User Exists");
        }
        updatedUser.get().setUsername(user.getUsername() != null && !user.getUsername().isBlank() ? user.getUsername() : updatedUser.get().getUsername());
        updatedUser.get().setEmail(user.getEmail() != null && !user.getEmail().isBlank() ? user.getEmail() : updatedUser.get().getEmail());
        updatedUser.get().setPassword(user.getPassword() != null && !user.getPassword().isBlank() ? user.getPassword() : updatedUser.get().getPassword());
        updatedUser.get().setRole(user.getRole() != null ? user.getRole() : updatedUser.get().getRole());
        repo.save(updatedUser.get());
        return ResponseEntity.ok(updatedUser.get());
    }

    public ResponseEntity<?> delete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ResponseEntity<Optional<userEntity>> userInDb = getUserByUsername(username);
        if (userInDb.getBody().isEmpty()){
            throw new noSuchEntityExists("No Such User Exists");
        }
        UUID id = userInDb.getBody().get().getId();
        repo.deleteById(id);
        return ResponseEntity.ok("User deleted Successfully");
    }

    public ResponseEntity<?> deleteById(UUID id) {
        Optional<userEntity> user = repo.findById(id);
        if (user.get() == null) {
            throw new noSuchEntityExists("No Such User Exists");
        }
        repo.deleteById(id);
        return ResponseEntity.ok("User deleted Successfully");
    }



    public validEntity validator(registerEntity user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ResponseEntity<Optional<userEntity>> userInDb = getUserByUsername(username);
        UUID id = userInDb.getBody().get().getId();
        userEntity updatingUser = new userEntity();
        updatingUser.setUsername(user.getUsername());
        updatingUser.setEmail(user.getEmail());
        updatingUser.setPassword(user.getPassword());
        updatingUser.setRole(Role.VIEWER);
        return new validEntity(id,updatingUser);
    }

    public ResponseEntity<Optional<userEntity>> getUserByUsername(String username) {
        Optional<userEntity> user = Optional.ofNullable(repo.findByUsername(username));
        if (user.isEmpty()) {
            throw new noSuchEntityExists("No Such User Exists");
        }
        return ResponseEntity.ok(user);
    }
}
