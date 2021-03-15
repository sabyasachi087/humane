package com.sroyc.humane.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.sroyc.humane.services.HumaneUserContext;

@TestConfiguration
public class HumaneTestConfiguration {

	private static final String USER = "TEST_USER";

	@Bean
	public HumaneUserContext userContext() {
		return new HumaneUserContext() {
			@Override
			public String loggedInUser() {
				return USER;
			}
		};
	}

}
