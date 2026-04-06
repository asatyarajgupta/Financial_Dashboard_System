package com.aditya.financial_dashboard_system;

import com.aditya.financial_dashboard_system.entities.userEntity;
import com.aditya.financial_dashboard_system.repos.userRepo;
import com.aditya.financial_dashboard_system.services.userService;
import com.aditya.financial_dashboard_system.utils.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class FinancialDashboardSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialDashboardSystemApplication.class, args);
    }
    @Bean
    public CommandLineRunner createAdmin(userService service, userRepo repo) {
        return args -> {
            if (repo.findByUsername("adi") == null) {
                userEntity admin = new userEntity();
                admin.setUsername("adi");
                admin.setEmail("adi@gmail.com");
                admin.setPassword("1611");
                admin.setRole(Role.ADMIN);
                service.saveUser(admin, Role.ADMIN);  // saveUser already encodes the password
            }
        };
    }

}
