package com.example.productsapibackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Products API Backend.
 * Provides CRUD operations for managing products with REST endpoints.
 */
@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Products API",
		version = "1.0.0",
		description = "REST API for managing products with CRUD operations. Supports creating, reading, updating (full and partial), and deleting products with fields: id, name, price, and quantity.",
		contact = @Contact(
			name = "Products API Support"
		)
	),
	servers = {
		@Server(url = "/", description = "Default Server URL")
	}
)
public class productsapibackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(productsapibackendApplication.class, args);
	}

}
