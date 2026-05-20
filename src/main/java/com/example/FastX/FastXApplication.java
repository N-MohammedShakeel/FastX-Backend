package com.example.FastX;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
		info = @Info(
				title = "FastX - Bus Ticket Booking System API Documentation",
				description = "Comprehensive REST API documentation for the FastX platform, handling Passengers, Operators, and Admin functionalities.",
				version = "v1.0",
				contact = @Contact(
						name = "FastX Support",
						email = "support@fastx.com",
						url = "https://www.fastx.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.apache.org/licenses/LICENSE-2.0"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "FastX Project GitHub Repository & Wiki",
				url = "https://github.com/your-username/FastX"
		)
)
@SecurityScheme(
		name = "Bearer Authentication",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer",
		description = "Enter your JWT token in the format: Bearer <token>"
)

public class

FastXApplication {
	public static void main(String[] args)
    {
		SpringApplication.run(FastXApplication.class, args);
	}
}
