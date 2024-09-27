package com.fenoreste.atms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@ComponentScan({"com.fenoreste.atms.controller",
	 			"com.fenoreste.atms.service",
	 			"com.fenoreste.atms.util",
	 			"com.fenoreste.atms.security"})
@EntityScan("com.fenoreste.atms.entity")
@EnableJpaRepositories("com.fenoreste.atms.repository")
public class AtmsApplication {

	public static void main(String[] args) {

		SpringApplication.run(AtmsApplication.class, args);
	}


	@PostConstruct
	public void init() {
		// Establecer la zona horaria predeterminada
		TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
	}
}
