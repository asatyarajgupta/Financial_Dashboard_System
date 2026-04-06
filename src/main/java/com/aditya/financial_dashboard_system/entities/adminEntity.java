package com.aditya.financial_dashboard_system.entities;

import com.aditya.financial_dashboard_system.utils.Role;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class adminEntity {
    private String username;
    private String password;
    @Email
    private String email;
    private Role role;
}
