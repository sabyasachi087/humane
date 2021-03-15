package com.sroyc.humane.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.Assert;

import com.sroyc.humane.data.model.mongo.MongoRoleMasterEntity;
import com.sroyc.humane.data.model.mongo.MongoUserMasterEntity;
import com.sroyc.humane.data.model.mongo.MongoUserRoleMapEntity;

@Profile("sroyc.data.mongo")
@Configuration
@EnableMongoRepositories(basePackages = "com.sroyc.humane.data.repo.mongo")
public class HumaneMongoConfiguration implements InitializingBean {

	private static final Logger LOGGER = LogManager.getLogger(HumaneMongoConfiguration.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(mongoTemplate, "Mongo Template is not available");
		this.createIndexes();
	}

	protected void createIndexes() {
		LOGGER.info("Ensuring index on role master ... ");
		this.mongoTemplate.indexOps(MongoRoleMasterEntity.class)
				.ensureIndex(new Index().on("roleCode", Direction.ASC).named("role_mst_code_uk").unique());

		LOGGER.info("Ensuring indexes on user master ... ");
		this.mongoTemplate.indexOps(MongoUserMasterEntity.class)
				.ensureIndex(new Index().on("username", Direction.ASC).named("usr_mst_username_uk").unique());
		this.mongoTemplate.indexOps(MongoUserMasterEntity.class)
				.ensureIndex(new Index().on("email", Direction.ASC).named("usr_mst_email_uk").unique());
		this.mongoTemplate.indexOps(MongoUserMasterEntity.class)
				.ensureIndex(new Index().on("fullname", Direction.ASC).named("usr_mst_fullname_indx").unique());

		LOGGER.info("Ensuring indexes on user role map ... ");
		this.mongoTemplate.indexOps(MongoUserRoleMapEntity.class).ensureIndex(
				new Index().on("userId", Direction.ASC).on("roleId", Direction.ASC).named("user_role_map_uk").unique());
	}

}
