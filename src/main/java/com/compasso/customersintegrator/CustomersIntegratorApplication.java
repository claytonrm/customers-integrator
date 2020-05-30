package com.compasso.customersintegrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CustomersIntegratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomersIntegratorApplication.class, args);
	}

}
