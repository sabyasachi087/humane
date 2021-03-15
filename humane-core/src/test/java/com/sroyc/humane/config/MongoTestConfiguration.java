package com.sroyc.humane.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Profile("sroyc.data.mongo")
@TestConfiguration
public class MongoTestConfiguration {

	private static final String DB_NAME = "INMEM_HUMANE_DB";

	@Bean
	public MongoClient mongoClient(@Autowired EmbeddedMongo mongo) throws IOException {
		return MongoClients.create(mongo.getConnectionUrl());
	}

	@Bean
	public MongoTemplate mongoTemplate(@Autowired MongoClient client) {
		return new MongoTemplate(client, DB_NAME);
	}

}
