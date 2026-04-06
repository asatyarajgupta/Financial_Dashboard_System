package com.aditya.financial_dashboard_system.entities;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class registerEntity {
    private String username;
    private String password;
    @Email
    private String email;
}
