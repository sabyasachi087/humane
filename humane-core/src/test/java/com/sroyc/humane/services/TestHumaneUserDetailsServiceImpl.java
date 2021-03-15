package com.sroyc.humane.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.util.CollectionUtils;

import com.sroyc.humane.data.dao.HumaneConfigurationSettingRepository;
import com.sroyc.humane.data.dao.HumaneRoleMasterRespository;
import com.sroyc.humane.data.dao.HumaneUserMasterRepository;
import com.sroyc.humane.data.dao.HumaneUserRoleMapRepository;
import com.sroyc.humane.data.dao.mongo.MongoTestDataCreator;
import com.sroyc.humane.data.model.EntityDataModelFactory;
import com.sroyc.humane.data.model.HumaneConfigurationSetting;
import com.sroyc.humane.data.model.HumaneRoleMaster;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.HumaneUserRoleMap;
import com.sroyc.humane.data.model.mongo.MongoUserMasterEntity;
import com.sroyc.humane.data.model.mongo.MongoUserRoleMapEntity;
import com.sroyc.humane.util.UniqueSequenceGenerator;
import com.sroyc.humane.view.models.HumaneUserDetails;

class TestHumaneUserDetailsServiceImpl {

	@Mock
	private HumaneUserMasterRepository userRepo;
	@Mock
	private HumaneUserRoleMapRepository userRoleMapRepo;
	@Mock
	private HumaneRoleMasterRespository roleRepo;
	@Mock
	private HumaneConfigurationSettingRepository configRepo;
	@Mock
	private EntityDataModelFactory entityModelFactory;
	@InjectMocks
	private HumaneUserDetailsServiceImpl hudService;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		Mockito.doAnswer(new Answer<HumaneUserMaster>() {

			@Override
			public HumaneUserMaster answer(InvocationOnMock invocation) throws Throwable {
				return new MongoUserMasterEntity();
			}

		}).when(this.entityModelFactory).user();

		Mockito.doAnswer(new Answer<HumaneUserMaster>() {

			@Override
			public HumaneUserMaster answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgument(0);
			}
		}).when(this.userRepo).createUser(Mockito.any(HumaneUserMaster.class));

		Mockito.doAnswer(new Answer<HumaneUserRoleMap>() {

			@Override
			public HumaneUserRoleMap answer(InvocationOnMock invocation) throws Throwable {
				return new MongoUserRoleMapEntity();
			}

		}).when(this.entityModelFactory).userRole();
	}

	@Test
	void test_loadUser_with_emailOrUsername() {
		HumaneUserMaster user = MongoTestDataCreator.createUser("TEST_USER", "user@email.com");
		user.setUserId(UniqueSequenceGenerator.CHAR16.next());
		Mockito.when(this.userRepo.findByEmailOrUsername("user@email.com")).thenReturn(Optional.of(user));
		HumaneUserDetails hud = this.hudService.loadUser("user@email.com");
		Assertions.assertNotNull(hud);
		Assertions.assertEquals("user@email.com", hud.getUser().getEmail());

		List<HumaneUserRoleMap> userRoleMap = this.getUserRoles(user.getUserId(), 2);
		Mockito.when(this.userRoleMapRepo.findAllMappingsByUser(user.getUserId())).thenReturn(userRoleMap);
		Mockito.doAnswer(new Answer<Optional<HumaneRoleMaster>>() {

			@Override
			public Optional<HumaneRoleMaster> answer(InvocationOnMock invocation) throws Throwable {
				HumaneRoleMaster role = MongoTestDataCreator.createRole("TEST_USER",
						RandomStringUtils.randomAlphabetic(3, 5));
				role.setRoleId(invocation.getArgument(0));
				return Optional.of(role);
			}
		}).when(this.roleRepo).findById(Mockito.anyString());
		hud = this.hudService.loadUser("user@email.com");
		Assertions.assertTrue(hud != null && !CollectionUtils.isEmpty(hud.getRoles()) && hud.getRoles().size() == 2);

	}

	private List<HumaneUserRoleMap> getUserRoles(String userId, int nbrOfRoles) {
		List<HumaneUserRoleMap> userRoles = new ArrayList<HumaneUserRoleMap>();
		for (int i = 0; i < nbrOfRoles; i++) {
			userRoles.add(
					MongoTestDataCreator.createUserRoleMap("TEST_USER", userId, UniqueSequenceGenerator.CHAR12.next()));
		}
		return userRoles;
	}

	@Test
	void test_loadUser_with_userAttributes() throws Exception {
		Map<HumaneConfigurationSetting.SettingKey, String> setting = this.getSettings();

		Mockito.when(this.configRepo.getUserAttributes()).thenReturn(this.getUserAttributes());
		Mockito.when(this.configRepo.getHumaneSettings()).thenReturn(setting);
		Map<String, String> attributes = this.createAttributes();
		HumaneUserDetails hud = this.hudService.loadUser(attributes);
		Assertions.assertNotNull(hud);
		Assertions.assertEquals(attributes.get("email"), hud.getUser().getEmail());
		Assertions.assertEquals(attributes.get("first_name"), hud.getUser().getFirstName());
		Assertions.assertEquals(attributes.get("last_name"), hud.getUser().getLastName());
		Assertions.assertEquals(attributes.get("username"), hud.getUser().getUsername());

		Assertions.assertNotNull(attributes.remove("username"));
		setting.put(HumaneConfigurationSetting.SettingKey.DEFAULT_ROLE, "R1");
		Mockito.when(this.configRepo.getHumaneSettings()).thenReturn(setting);
		HumaneRoleMaster role = MongoTestDataCreator.createRole("TEST_USER", "R1");
		Mockito.when(this.roleRepo.findByRoleCode("R1")).thenReturn(Optional.of(role));
		hud = this.hudService.loadUser(attributes);
		Assertions.assertNotNull(hud.getRoles());
		Assertions.assertEquals(1, hud.getRoles().size());
		Mockito.verify(this.userRoleMapRepo, Mockito.times(1)).create(Mockito.any(HumaneUserRoleMap.class));
		Assertions.assertEquals(hud.getUser().getEmail(), hud.getUser().getUsername());
	}

	protected Map<HumaneConfigurationSetting.Attribute, String> getUserAttributes() {
		Map<HumaneConfigurationSetting.Attribute, String> userAttr = new HashMap<>();
		userAttr.put(HumaneConfigurationSetting.Attribute.EMAIL, "email");
		userAttr.put(HumaneConfigurationSetting.Attribute.USERNAME, "username");
		userAttr.put(HumaneConfigurationSetting.Attribute.LAST_NAME, "last_name");
		userAttr.put(HumaneConfigurationSetting.Attribute.FIRST_NAME, "first_name");
		return userAttr;
	}

	protected Map<HumaneConfigurationSetting.SettingKey, String> getSettings() {
		Map<HumaneConfigurationSetting.SettingKey, String> setting = new HashMap<>();
		setting.put(HumaneConfigurationSetting.SettingKey.AUTO_CREATE_USER, String.valueOf(true));
		setting.put(HumaneConfigurationSetting.SettingKey.DEFAULT_ROLE, null);
		return setting;
	}

	protected Map<String, String> createAttributes() {
		Map<String, String> attributes = new HashMap<>();
		attributes.put("email", "user@email.com");
		attributes.put("username", "user");
		attributes.put("last_name", "user_first");
		attributes.put("first_name", "user_last");
		return attributes;
	}

}
