package com.LoboProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.LoboProject.config.property.loboApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(loboApiProperty.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
