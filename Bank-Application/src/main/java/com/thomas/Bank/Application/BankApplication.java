package com.thomas.Bank.Application;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Bank Application",
				description = "Summary about the APIs used in Bank Application",
				version = "1.0",
				contact = @Contact(
						name = "Thomas Kovoor",
						email = "thomaskovoor123@gmail.com",
                        url = "https://github.com/thomaskovoor/Bank-Application-using-Spring-Boot"
				),
                license = @License(
						name = "Bank Application License",
						url = "https://github.com/thomaskovoor/Bank-Application-using-Spring-Boot"
				),
				summary = "Details about APIs"
		) ,
		externalDocs = @ExternalDocumentation(
				description = "Bank Application Documentation",
				url = "https://github.com/thomaskovoor/Bank-Application-using-Spring-Boot"
		)
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
