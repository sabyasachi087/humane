package com.sroyc.humane.api.v1;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sroyc.humane.api.v1.AbstractTestController.HumaneTestControllerApplication;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HumaneTestControllerApplication.class })
public abstract class AbstractTestController {

	private final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	protected ObjectMapper objectMapper() {
		return this.mapper;
	}

	public MockMvc getMockMvc() {
		return mockMvc;
	}

	@SpringBootApplication
	static class HumaneTestControllerApplication {

		public static void main(String[] args) {
			SpringApplication.run(HumaneTestControllerApplication.class, args);
		}

	}

}
