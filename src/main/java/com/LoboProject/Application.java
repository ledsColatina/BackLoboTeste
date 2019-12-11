package com.LoboProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.LoboProject.config.property.loboApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(loboApiProperty.class)
public class Application {
//Comentario
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("admin"));
		SpringApplication.run(Application.class, args);
	}

}
