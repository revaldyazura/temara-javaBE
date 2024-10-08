package com.temara.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({
		"com.temara.backend.config",
		"com.temara.backend.controller",
		"com.temara.backend.service",
		"com.temara.backend.security",
		"com.temara.backend.entity",
		"com.temara.backend.aop",
})
@EnableJpaRepositories("com.temara.backend.repository")
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
