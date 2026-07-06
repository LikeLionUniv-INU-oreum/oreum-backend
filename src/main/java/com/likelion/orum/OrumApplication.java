package com.likelion.orum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OrumApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrumApplication.class, args);
	}

}
