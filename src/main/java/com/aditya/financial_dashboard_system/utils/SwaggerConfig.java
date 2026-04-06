package com.aditya.financial_dashboard_system.utils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        SecurityScheme jwtScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Bearer Authentication");

        return new OpenAPI()
                .info(new Info()
                        .title("Financial Dashboard System API")
                        .description("""
                                REST API for managing personal financial records.
                                
                                **How to use:**
                                1. Register via `POST /api/public/create`
                                2. Login via `POST /api/public/login` to get your JWT token
                                3. Click the **Authorize** button above and paste your token
                                4. All protected endpoints will now work
                                
                                **Roles:** VIEWER → ANALYST → ADMIN (each role includes the previous)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Aditya")
                                .email("aditya@example.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", jwtScheme));
    }
}