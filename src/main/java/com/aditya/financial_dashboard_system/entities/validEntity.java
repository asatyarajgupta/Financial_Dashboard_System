package com.aditya.financial_dashboard_system.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class validEntity {
    private UUID id;
    private userEntity entity;
}
