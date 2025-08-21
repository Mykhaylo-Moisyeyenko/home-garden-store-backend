package com.homegarden.store.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Home Garden Store Backend API",
                description = "OpenAPI 3.1 specification for the backend service of an online garden store",
                version = "3.1.0",
                contact = @Contact(
                        name = "Team - 2 (Backend Developers)",
                        email = "Team2@gmail.com",
                        url = "https://github.com/Mykhaylo-Moisyeyenko/home-garden-store-backend"
                )
        )
)

public class Swagger {

}