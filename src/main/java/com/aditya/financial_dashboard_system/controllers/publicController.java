package com.aditya.financial_dashboard_system.controllers;
import com.aditya.financial_dashboard_system.entities.registerEntity;
import com.aditya.financial_dashboard_system.entities.userEntity;
import com.aditya.financial_dashboard_system.entities.validEntity;
import com.aditya.financial_dashboard_system.services.authService;
import com.aditya.financial_dashboard_system.services.userService;
import com.aditya.financial_dashboard_system.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class publicController {
    private final userService service;
    private final authService auth;


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody registerEntity user) {
        userEntity registeringUser = new userEntity();
        registeringUser.setUsername(user.getUsername());
        registeringUser.setEmail(user.getEmail());
        registeringUser.setPassword(user.getPassword());
        return service.saveUser(registeringUser, Role.VIEWER);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody registerEntity user) {
        return auth.loginService(user);
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody registerEntity user) {
        validEntity validated_user = service.validator(user);
        return service.updateUser(validated_user.getId(), validated_user.getEntity());
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUsers() {
        return service.delete();
    }

}
