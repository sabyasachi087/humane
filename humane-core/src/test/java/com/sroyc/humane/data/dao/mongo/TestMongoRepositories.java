package com.sroyc.humane.data.dao.mongo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sroyc.humane.config.HumaneApplication;
import com.sroyc.humane.data.dao.HumaneUserMasterFilter;
import com.sroyc.humane.data.model.Address;
import com.sroyc.humane.data.model.HumaneConfigurationSetting;
import com.sroyc.humane.data.model.HumaneConfigurationSetting.SettingType;
import com.sroyc.humane.data.model.HumaneRoleMaster;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.HumaneUserRoleMap;
import com.sroyc.humane.data.model.Phone;
import com.sroyc.humane.data.model.mongo.MongoAddressData;
import com.sroyc.humane.data.model.mongo.MongoConfigurationSettingEntity;
import com.sroyc.humane.data.model.mongo.MongoPhoneData;
import com.sroyc.humane.data.model.mongo.MongoRoleMasterEntity;
import com.sroyc.humane.data.model.mongo.MongoUserMasterEntity;
import com.sroyc.humane.data.model.mongo.MongoUserRoleMapEntity;
import com.sroyc.humane.services.HumaneUserContext;
import com.sroyc.humane.view.models.HumaneUserRoleView;

@SpringBootTest(classes = { HumaneApplication.class })
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = { "sroyc.data.mongo" })
class TestMongoRepositories {

	@Autowired
	private MongoHumaneRoleMasterRespositoryImpl roleRepo;
	@Autowired
	private MongoHumaneUserMasterRepositoryImpl userRepo;
	@Autowired
	private MongoHumaneUserRoleMapRepositoryImpl userRoleRepo;
	@Autowired
	private MongoHumaneConfigurationSettingRepositoryImpl configSettingRepo;
	@Autowired
	private HumaneUserContext userContext;
	@Autowired
	private ApplicationContext context;

	private MongoTemplate mongoTemplate;
	private Random random = new Random(System.currentTimeMillis());
	private String userEmail = "abc@email.com";

	@BeforeEach
	void initialize() {
		Assertions.assertNotNull(roleRepo);
		Assertions.assertNotNull(userRepo);
		Assertions.assertNotNull(userRoleRepo);
		Assertions.assertNotNull(configSettingRepo);
		if (this.mongoTemplate == null) {
			this.mongoTemplate = this.context.getBean(MongoTemplate.class);
		}
		this.deleteAllUserRoleMappings();
		this.deleteAllUser();
		this.deleteAllRoles();
		this.deleteAllConfigSettings();
	}

	@Test
	void testUserMasterRepo_1() {
		MongoUserMasterEntity entity = MongoTestDataCreator.createUser(this.userContext.loggedInUser(), this.userEmail);
		entity = (MongoUserMasterEntity) this.userRepo.createUser(entity);
		Assertions.assertNotNull(entity);
		Assertions.assertEquals(0, entity.getVersion());
		Optional<HumaneUserMaster> user = this.userRepo.findByEmailOrUsername(userEmail);
		Assertions.assertTrue(user.isPresent());
		Assertions.assertEquals(entity.getUserId(), user.get().getUserId());
		entity = (MongoUserMasterEntity) user.get();
		entity.setFirstName("FirstName1");
		entity.setModifiedBy(this.userContext.loggedInUser());
		entity.setModifiedOn(LocalDateTime.now());
		entity = (MongoUserMasterEntity) this.userRepo.updateUser(entity);
		Assertions.assertEquals(1, entity.getVersion());
		Assertions.assertEquals("FirstName1", entity.getFirstName());
		Assertions.assertEquals(this.userContext.loggedInUser(), entity.getModifiedBy());
		Assertions.assertTrue(this.userRepo.findById(entity.getUserId()).isPresent());
		Assertions.assertTrue(this.userRepo.findById("OOWEE").isEmpty());
		entity = (MongoUserMasterEntity) this.userRepo.toggleActivation(entity.getUserId(), false,
				this.userContext.loggedInUser());
		Assertions.assertEquals(2, entity.getVersion());
		Assertions.assertFalse(entity.isActive());
		entity = (MongoUserMasterEntity) this.userRepo.toggleDeletion(entity.getUserId(), true,
				this.userContext.loggedInUser());
		Assertions.assertEquals(3, entity.getVersion());
		Assertions.assertTrue(entity.isDelete());
	}

	@Test
	void testUserMasterRepo_2() {
		List<HumaneUserMaster> users = this.createRandomUsers(20);
		HumaneUserMaster randUser = users.get(this.random.nextInt(20));
		String filtername = randUser.getFirstName();
		HumaneUserMasterFilter filter = HumaneUserMasterFilter.build(filtername, true, false);
		List<HumaneUserMaster> filterUsers = this.userRepo.findAllWithFilters(filter,
				PageRequest.of(0, 5, Direction.ASC, "userId"));
		Assertions.assertTrue(filterUsers.size() > 0);
		int count = filterUsers.size();
		filtername = randUser.getFirstName().substring(2, randUser.getFirstName().length() - 2);
		filter = HumaneUserMasterFilter.build(filtername, true, false);
		Assertions.assertEquals(count, filterUsers.size());
		filtername = randUser.getFirstName().substring(2, randUser.getFirstName().length() - 2).toLowerCase();
		filter = HumaneUserMasterFilter.build(filtername, true, false);
		Assertions.assertEquals(count, filterUsers.size());
	}

	@Test
	void testUserMasterRepo_3() {
		HumaneUserMaster user = MongoTestDataCreator.createUser(this.userContext.loggedInUser(), this.userEmail);
		MongoPhoneData pn1 = MongoTestDataCreator.createPhoneData();
		List<MongoPhoneData> phones = new ArrayList<>();
		phones.add(pn1);
		user.setPhoneNumbers(phones);
		user = this.userRepo.createUser(user);
		MongoPhoneData pn2 = MongoTestDataCreator.createPhoneData();
		phones.clear();
		phones.add(pn2);
		this.userRepo.replacePhoneNumbers(user.getUserId(), this.userContext.loggedInUser(), phones);
		List<Phone> updatedPhoneList = this.userRepo.getPhoneNumbers(user.getUserId());
		Assertions.assertEquals(phones.size(), updatedPhoneList.size());
		Assertions.assertEquals(phones.get(0).getPhoneNumber(), updatedPhoneList.get(0).getPhoneNumber());
	}

	@Test
	void testUserMasterRepo_4() {
		HumaneUserMaster user = MongoTestDataCreator.createUser(this.userContext.loggedInUser(), this.userEmail);
		MongoAddressData addrs = MongoTestDataCreator.createAddressData();
		List<MongoAddressData> addresses = new ArrayList<>();
		addresses.add(addrs);
		// user.setAddresses(addresses);
		user = this.userRepo.createUser(user);
		this.userRepo.replaceAddresses(user.getUserId(), this.userContext.loggedInUser(), addresses);
		List<Address> updatedAddressList = this.userRepo.getAddresses(user.getUserId());
		Assertions.assertEquals(addresses.size(), updatedAddressList.size());
		Assertions.assertEquals(addresses.get(0).getId(), updatedAddressList.get(0).getId());
	}

	@Test
	void testRoleMasterRepo_1() {
		HumaneRoleMaster role = MongoTestDataCreator.createRole(this.userEmail, "ROLE1");
		role = this.roleRepo.createRole(role);
		Assertions.assertNotNull(role.getRoleId());
		role = this.roleRepo.updateRole(role.getRoleId(), "ROLE_UPDATED", 1, this.userContext.loggedInUser());
		Assertions.assertEquals("ROLE_UPDATED", role.getRoleName());
		Assertions.assertEquals(1, role.getHierarchy());
		Assertions.assertEquals(this.userContext.loggedInUser(), role.getModifiedBy());
		this.roleRepo.deprecate(role.getRoleId(), true, this.userContext.loggedInUser());
		Optional<HumaneRoleMaster> data = this.roleRepo.findById(role.getRoleId());
		Assertions.assertTrue(data.isPresent());
		role = data.get();
		Assertions.assertTrue(role.isDeprecate());
		data = this.roleRepo.findByRoleCode("ROLE1");
		Assertions.assertTrue(data.isPresent() && role.getRoleId().equals(data.get().getRoleId()));
		this.roleRepo.delete(role.getRoleId());
		data = this.roleRepo.findByRoleCode("ROLE1");
		Assertions.assertTrue(data.isEmpty());
	}

	@Test
	void testRoleMasterRepo_2() {
		List<HumaneRoleMaster> roles = this.createRandomRoles(5);
		roles.forEach(role -> {
			this.roleRepo.createRole(role);
		});
		List<HumaneRoleMaster> allRoles = this.roleRepo.findAll();
		Assertions.assertEquals(roles.size(), allRoles.size());
	}

	@Test
	void testUserRoleMpRepo() {
		HumaneUserRoleMap userRole = MongoTestDataCreator.createUserRoleMap(this.userContext.loggedInUser(), "U1",
				"R1");
		userRole = this.userRoleRepo.create(userRole);
		Assertions.assertTrue(userRole != null && StringUtils.hasText(userRole.getUserRoleId()));
		userRole = MongoTestDataCreator.createUserRoleMap(this.userContext.loggedInUser(), "U1", "R2");
		userRole = this.userRoleRepo.create(userRole);
		userRole = MongoTestDataCreator.createUserRoleMap(this.userContext.loggedInUser(), "U2", "R2");
		userRole = this.userRoleRepo.create(userRole);
		userRole = MongoTestDataCreator.createUserRoleMap(this.userContext.loggedInUser(), "U2", "R3");
		userRole = this.userRoleRepo.create(userRole);
		List<HumaneUserRoleMap> userRoleMappings = this.userRoleRepo.findAllMappingsByRole("R2",
				PageRequest.of(0, 1, Direction.ASC, "userRoleId"));
		Assertions.assertEquals(1, userRoleMappings.size());
		userRoleMappings = this.userRoleRepo.findAllMappingsByRole("R2",
				PageRequest.of(1, 1, Direction.ASC, "userRoleId"));
		Assertions.assertEquals(1, userRoleMappings.size());
		userRoleMappings = this.userRoleRepo.findAllMappingsByRole("R2",
				PageRequest.of(0, 10, Direction.ASC, "userRoleId"));
		Assertions.assertEquals(2, userRoleMappings.size());
		userRoleMappings = this.userRoleRepo.findAllMappingsByUser("U1");
		Assertions.assertEquals(2, userRoleMappings.size());
		Optional<HumaneUserRoleMap> data = this.userRoleRepo.findByUserAndRoleId("U1", "R3");
		Assertions.assertTrue(data.isEmpty());
		data = this.userRoleRepo.findByUserAndRoleId("U1", "R2");
		Assertions.assertTrue(data.isPresent());
		this.userRoleRepo.deleteAllMappings("U1");
		userRoleMappings = this.userRoleRepo.findAllMappingsByUser("U1");
		Assertions.assertTrue(userRoleMappings == null || userRoleMappings.isEmpty());
		data = this.userRoleRepo.findByUserAndRoleId("U2", "R3");
		Assertions.assertTrue(data.isPresent());
		this.userRoleRepo.delete(data.get().getUserRoleId());
		data = this.userRoleRepo.findByUserAndRoleId("U2", "R3");
		Assertions.assertTrue(data.isEmpty());
	}

	@Test
	void testUserRoleMpRepo_getMappingView() {
		MongoRoleMasterEntity rm1 = MongoTestDataCreator.createRole(this.userContext.loggedInUser(), "R1");
		rm1.setRoleName("Role One");
		rm1 = (MongoRoleMasterEntity) this.roleRepo.createRole(rm1);
		MongoRoleMasterEntity rm2 = MongoTestDataCreator.createRole(this.userContext.loggedInUser(), "R2");
		rm2.setRoleName("Role Two");
		rm2 = (MongoRoleMasterEntity) this.roleRepo.createRole(rm2);

		HumaneUserRoleMap userRole = MongoTestDataCreator.createUserRoleMap(this.userContext.loggedInUser(), "U1",
				rm1.getRoleId());
		userRole = this.userRoleRepo.create(userRole);
		userRole = MongoTestDataCreator.createUserRoleMap(this.userContext.loggedInUser(), "U1", rm2.getRoleId());
		userRole = this.userRoleRepo.create(userRole);
		List<HumaneUserRoleView> userRoles = this.userRoleRepo.findMappingViewOfUser("U1");
		Assertions.assertEquals(2, userRoles.size());
		Assertions.assertNotNull(userRoles.get(0).getRoleName());
		Assertions.assertNotNull(userRoles.get(0).getRoleCode());
	}

	@Test
	void test_configSettingRepo_userAttributes() {
		Map<HumaneConfigurationSetting.Attribute, String> setting = this.getDefaultSetting();
		String id = this.configSettingRepo.saveUserAttributes(setting, this.userContext.loggedInUser());
		Assertions.assertNotNull(id);
		Map<HumaneConfigurationSetting.Attribute, String> settingFetched = this.configSettingRepo.getUserAttributes();
		Assertions.assertTrue(!CollectionUtils.isEmpty(settingFetched));
		for (Entry<HumaneConfigurationSetting.Attribute, String> record : setting.entrySet()) {
			Assertions.assertEquals(setting.get(record.getKey()), settingFetched.get(record.getKey()));
		}

		settingFetched.put(HumaneConfigurationSetting.Attribute.USERNAME, "user_name");
		this.configSettingRepo.saveUserAttributes(settingFetched, this.userContext.loggedInUser());
		settingFetched = this.configSettingRepo.getUserAttributes();
		Assertions.assertEquals("user_name", settingFetched.get(HumaneConfigurationSetting.Attribute.USERNAME));

		this.configSettingRepo.delete(SettingType.USER_ATTRIBUTES);
		settingFetched = this.configSettingRepo.getUserAttributes();
		Assertions.assertTrue(CollectionUtils.isEmpty(settingFetched));
	}

	private Map<HumaneConfigurationSetting.Attribute, String> getDefaultSetting() {
		Map<HumaneConfigurationSetting.Attribute, String> setting = new EnumMap<>(
				HumaneConfigurationSetting.Attribute.class);
		setting.put(HumaneConfigurationSetting.Attribute.EMAIL, "email");
		setting.put(HumaneConfigurationSetting.Attribute.FIRST_NAME, "firstName");
		setting.put(HumaneConfigurationSetting.Attribute.LAST_NAME, "lastName");
		setting.put(HumaneConfigurationSetting.Attribute.USERNAME, "username");
		return setting;
	}

	private List<HumaneRoleMaster> createRandomRoles(int nbrOfRoles) {
		List<HumaneRoleMaster> roles = new ArrayList<>();
		while (nbrOfRoles-- > 0) {
			HumaneRoleMaster role = MongoTestDataCreator.createRole(this.userContext.loggedInUser(),
					RandomStringUtils.randomAlphabetic(5));
			roles.add(role);
		}
		return roles;
	}

	private List<HumaneUserMaster> createRandomUsers(int numberOfUsers) {
		List<HumaneUserMaster> users = new ArrayList<>();
		while (numberOfUsers-- > 0) {
			HumaneUserMaster user = MongoTestDataCreator.createUser(this.userContext.loggedInUser(),
					RandomStringUtils.randomAlphanumeric(7) + "@email.com");
			users.add(this.userRepo.createUser(user));
		}
		return users;
	}

	private void deleteAllUser() {
		Query q = new Query();
		q.addCriteria(Criteria.where("userId").exists(true));
		this.mongoTemplate.findAllAndRemove(q, MongoUserMasterEntity.class);
	}

	private void deleteAllRoles() {
		Query q = new Query();
		q.addCriteria(Criteria.where("roleId").exists(true));
		this.mongoTemplate.findAllAndRemove(q, MongoRoleMasterEntity.class);
	}

	private void deleteAllUserRoleMappings() {
		Query q = new Query();
		q.addCriteria(Criteria.where("userRoleId").exists(true));
		this.mongoTemplate.findAllAndRemove(q, MongoUserRoleMapEntity.class);
	}

	private void deleteAllConfigSettings() {
		Query q = new Query();
		q.addCriteria(Criteria.where("id").exists(true));
		this.mongoTemplate.findAllAndRemove(q, MongoConfigurationSettingEntity.class);
	}

}
