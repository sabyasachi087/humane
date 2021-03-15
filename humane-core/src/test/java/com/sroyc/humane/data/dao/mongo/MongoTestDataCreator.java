package com.sroyc.humane.data.dao.mongo;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;

import com.sroyc.humane.data.model.mongo.MongoAddressData;
import com.sroyc.humane.data.model.mongo.MongoPhoneData;
import com.sroyc.humane.data.model.mongo.MongoRoleMasterEntity;
import com.sroyc.humane.data.model.mongo.MongoUserMasterEntity;
import com.sroyc.humane.data.model.mongo.MongoUserRoleMapEntity;
import com.sroyc.humane.util.UniqueSequenceGenerator;

public class MongoTestDataCreator {

	private MongoTestDataCreator() {
		super();
	}

	public static final MongoUserMasterEntity createUser(String createdBy, String email) {
		MongoUserMasterEntity entity = new MongoUserMasterEntity();
		entity.setActive(true);
		entity.setCreatedBy(createdBy);
		entity.setCreatedOn(LocalDateTime.now());
		entity.setEmail(email);
		String firstName = RandomStringUtils.randomAlphabetic(10);
		String lastName = RandomStringUtils.randomAlphabetic(10);
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		entity.setFullname(firstName + " " + lastName);
		entity.setUsername(email);
		return entity;
	}

	public static final MongoRoleMasterEntity createRole(String createdBy, String roleCode) {
		MongoRoleMasterEntity entity = new MongoRoleMasterEntity();
		entity.setCreatedBy(createdBy);
		entity.setCreatedOn(LocalDateTime.now());
		entity.setRoleCode(roleCode);
		entity.setRoleName("Role name for " + roleCode);
		return entity;
	}

	public static final MongoUserRoleMapEntity createUserRoleMap(String createdBy, String userId, String roleId) {
		MongoUserRoleMapEntity entity = new MongoUserRoleMapEntity();
		entity.setCreatedBy(createdBy);
		entity.setCreatedOn(LocalDateTime.now());
		entity.setRoleId(roleId);
		entity.setUserId(userId);
		return entity;
	}

	public static final MongoPhoneData createPhoneData() {
		MongoPhoneData phone = new MongoPhoneData();
		phone.setCountryCode(RandomStringUtils.randomNumeric(3));
		phone.setPhoneNumber(RandomStringUtils.randomNumeric(10));
		phone.setPrimary(true);
		return phone;
	}

	public static final MongoAddressData createAddressData() {
		MongoAddressData data = new MongoAddressData();
		data.setCity(RandomStringUtils.randomAlphabetic(10));
		data.setCodeOrPin(RandomStringUtils.randomNumeric(7));
		data.setCountry(RandomStringUtils.randomAlphabetic(2));
		data.setId(UniqueSequenceGenerator.CHAR12.next());
		data.setPrimary(true);
		data.setLine1(RandomStringUtils.randomAlphabetic(100));
		data.setState(RandomStringUtils.randomAlphabetic(12));
		return data;
	}

}
