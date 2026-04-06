package com.aditya.financial_dashboard_system.repos;

import com.aditya.financial_dashboard_system.entities.userEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface userRepo extends CrudRepository<userEntity, UUID> {

    userEntity findByUsername(String username);
}
