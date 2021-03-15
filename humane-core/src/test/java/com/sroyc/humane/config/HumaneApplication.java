package com.sroyc.humane.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

@SpringBootApplication(exclude = { EmbeddedMongoAutoConfiguration.class })
@EnableHumaneCore
public class HumaneApplication {

	public static void main(String[] args) {
		SpringApplication.run(HumaneApplication.class, args);
	}

}
