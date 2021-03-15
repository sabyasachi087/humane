package com.sroyc.humane.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import com.sroyc.humane.api.v1.HumaneConfgurableProperty;
import com.sroyc.humane.data.dao.HumaneRoleMasterRespository;
import com.sroyc.humane.data.dao.HumaneUserMasterFilter;
import com.sroyc.humane.data.dao.HumaneUserMasterRepository;
import com.sroyc.humane.data.dao.HumaneUserRoleMapRepository;
import com.sroyc.humane.data.dao.mongo.MongoTestDataCreator;
import com.sroyc.humane.data.model.Address;
import com.sroyc.humane.data.model.EntityDataModelFactory;
import com.sroyc.humane.data.model.HumaneRoleMaster;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.HumaneUserRoleMap;
import com.sroyc.humane.data.model.Phone;
import com.sroyc.humane.data.model.mongo.MongoAddressData;
import com.sroyc.humane.data.model.mongo.MongoPhoneData;
import com.sroyc.humane.data.model.mongo.MongoRoleMasterEntity;
import com.sroyc.humane.data.model.mongo.MongoUserMasterEntity;
import com.sroyc.humane.data.model.mongo.MongoUserRoleMapEntity;
import com.sroyc.humane.exception.HumaneEntityViolationException;
import com.sroyc.humane.util.CommonUtils;
import com.sroyc.humane.util.UniqueSequenceGenerator;
import com.sroyc.humane.util.ValueObjectAdapter;
import com.sroyc.humane.view.models.AddressVO;
import com.sroyc.humane.view.models.HumaneRoleMasterVO;
import com.sroyc.humane.view.models.HumaneUserMasterVO;
import com.sroyc.humane.view.models.HumaneUserRoleMapVO;
import com.sroyc.humane.view.models.PhoneNumberVO;

class TestHumaneServiceImpl {

	@Mock
	private HumaneUserMasterRepository userRepo;
	@Mock
	private HumaneUserRoleMapRepository userRoleMapRepo;
	@Mock
	private HumaneRoleMasterRespository roleRepo;
	@Mock
	private HumaneConfgurableProperty configProperty;
	@Mock
	private HumaneUserContext userContext;
	@Mock
	private EntityDataModelFactory entityModelFactory;
	@InjectMocks
	private HumaneServiceImpl humaneService;

	private String userEmail = "user@email.com";

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		Mockito.when(this.userContext.loggedInUser()).thenReturn("TEST_USER");
	}

	@Test
	void test_findByEmailOrUsername() {
		HumaneUserMaster testUser = MongoTestDataCreator.createUser(this.userContext.loggedInUser(), this.userEmail);
		Mockito.when(this.userRepo.findByEmailOrUsername(userEmail)).thenReturn(Optional.of(testUser));
		HumaneUserMasterVO vo = this.humaneService.findByEmailOrUsername(userEmail);
		Assertions.assertEquals(testUser.getFirstName(), vo.getFirstName());
		Assertions.assertEquals(testUser.getLastName(), vo.getLastName());
		Assertions.assertEquals(testUser.getEmail(), vo.getEmail());
		vo = this.humaneService.findByEmailOrUsername("oola@poola.com");
		Assertions.assertNull(vo);
	}

	@Test
	void test_findByUserId() {
		HumaneUserMaster testUser = MongoTestDataCreator.createUser(this.userContext.loggedInUser(), this.userEmail);
		testUser.setUserId(UniqueSequenceGenerator.CHAR16.next());
		Mockito.when(this.userRepo.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
		HumaneUserMasterVO vo = this.humaneService.findByUserId(testUser.getUserId());
		Assertions.assertNotNull(vo);
		vo = this.humaneService.findByUserId(userEmail);
		Assertions.assertNull(vo);
	}

	@Test
	void test_findAllWithFilter() {
		HumaneUserMasterFilter filter = HumaneUserMasterFilter.build("papa", true, true);
		this.mock_findAllWithFilters(0, 10, "userId");
		List<HumaneUserMasterVO> vos = this.humaneService.findAllWithFilter(filter, 0, 10);
		Assertions.assertEquals(2, vos.size());
		Mockito.when(this.userRepo.findAllWithFilters(Mockito.any(HumaneUserMasterFilter.class),
				Mockito.any(Pageable.class))).thenReturn(null);
		vos = this.humaneService.findAllWithFilter(filter, 0, 10);
		Assertions.assertTrue(vos.isEmpty());
	}

	protected void mock_findAllWithFilters(Integer pageNumber, Integer pageSize, String property) {
		Mockito.doAnswer(new Answer<List<HumaneUserMaster>>() {

			@Override
			public List<HumaneUserMaster> answer(InvocationOnMock invocation) throws Throwable {
				List<HumaneUserMaster> hums = new ArrayList<>();
				Pageable page = invocation.getArgument(1);
				Assertions.assertEquals(pageNumber, page.getPageNumber());
				Assertions.assertEquals(pageSize, page.getPageSize());
				Assertions.assertEquals(Order.asc(property), page.getSort().getOrderFor(property));
				hums.add(MongoTestDataCreator.createUser(userContext.loggedInUser(), "1@email.com"));
				hums.add(MongoTestDataCreator.createUser(userContext.loggedInUser(), "2@email.com"));
				return hums;
			}
		}).when(this.userRepo).findAllWithFilters(Mockito.any(HumaneUserMasterFilter.class),
				Mockito.any(Pageable.class));
	}

	@Test
	void test_createUser() {
		Mockito.when(this.entityModelFactory.user()).thenReturn(new MongoUserMasterEntity());
		HumaneUserMasterVO vo = TestDataCreator.createUserVO("abc@email.com");
		Mockito.doAnswer(new Answer<HumaneUserMaster>() {

			@Override
			public HumaneUserMaster answer(InvocationOnMock invocation) throws Throwable {
				HumaneUserMaster user = invocation.getArgument(0);
				Assertions.assertTrue(StringUtils.hasText(user.getFullname()));
				user.setUserId(UniqueSequenceGenerator.CHAR16.next());
				return user;
			}

		}).when(this.userRepo).createUser(Mockito.any(HumaneUserMaster.class));
		String userId = this.humaneService.createUser(vo);
		Assertions.assertTrue(StringUtils.hasText(userId));
	}

	@Test
	void test_updateUser() {
		HumaneUserMaster user = MongoTestDataCreator.createUser(this.userContext.loggedInUser(), "abc@email.com");
		HumaneUserMasterVO vo = TestDataCreator.createUserVO("abc@email.com");
		vo.setUserId(UniqueSequenceGenerator.CHAR16.next());
		user.setUserId(vo.getUserId());
		Mockito.when(this.userRepo.findById(vo.getUserId())).thenReturn(Optional.of(user));
		Mockito.doAnswer(new Answer<HumaneUserMaster>() {

			@Override
			public HumaneUserMaster answer(InvocationOnMock invocation) throws Throwable {
				HumaneUserMaster user = invocation.getArgument(0);
				Assertions.assertEquals(CommonUtils.fullname(vo.getFirstName(), vo.getLastName(), vo.getMiddleName()),
						user.getFullname());
				return user;
			}
		}).when(this.userRepo).updateUser(Mockito.any(HumaneUserMaster.class));
		Assertions.assertTrue(this.humaneService.updateUser(vo));
		Mockito.reset(this.userRepo);
		Mockito.when(this.userRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
		Assertions.assertFalse(this.humaneService.updateUser(vo));
	}

	@Test
	void test_toggleDeletion() {
		Mockito.when(this.userRepo.toggleDeletion(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString()))
				.thenReturn(Mockito.mock(HumaneUserMaster.class));
		Assertions.assertTrue(this.humaneService.toggleDeletion("dsfdsf", true));
		Mockito.reset(this.userRepo);
		Mockito.when(this.userRepo.toggleDeletion(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString()))
				.thenReturn(null);
		Assertions.assertFalse(this.humaneService.toggleDeletion("dsfdsf", true));
	}

	@Test
	void test_toggleActivation() {
		Mockito.when(this.userRepo.toggleActivation(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString()))
				.thenReturn(Mockito.mock(HumaneUserMaster.class));
		Assertions.assertTrue(this.humaneService.toggleActivation("dsfdsf", true));
		Mockito.reset(this.userRepo);
		Mockito.when(this.userRepo.toggleActivation(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString()))
				.thenReturn(null);
		Assertions.assertFalse(this.humaneService.toggleActivation("dsfdsf", true));
	}

	@Test
	void test_addOrUpdatePhoneRecord_1() {
		Mockito.doAnswer(new Answer<Phone>() {

			@Override
			public Phone answer(InvocationOnMock invocation) throws Throwable {
				return new MongoPhoneData();
			}
		}).when(this.entityModelFactory).phone();
		PhoneNumberVO vo1 = TestDataCreator.createPhoneVO();
		vo1.setPrimary(true);
		PhoneNumberVO vo2 = TestDataCreator.createPhoneVO();
		List<Phone> phones = this.humaneService.addOrUpdatePhoneRecord(Collections.emptyList(), vo1);
		Assertions.assertEquals(1, phones.size());
		phones = this.humaneService.addOrUpdatePhoneRecord(phones, vo2);
		Assertions.assertEquals(2, phones.size());
		PhoneNumberVO vo1Similar = TestDataCreator.createPhoneVO();
		vo1Similar.setPrimary(false);
		vo1Similar.setPhoneNumber(vo1.getPhoneNumber());
		vo1Similar.setCountryCode(vo1.getCountryCode());
		phones = this.humaneService.addOrUpdatePhoneRecord(phones, vo1Similar);
		Assertions.assertEquals(2, phones.size());
		Optional<Phone> phone1 = phones.stream().filter(pn -> pn.getPhoneNumber().equals(vo1Similar.getPhoneNumber()))
				.findAny();
		Assertions.assertTrue(phone1.isPresent() && !phone1.get().isPrimary());
	}

	private List<Phone> getRandomPhones(Integer size) {
		List<Phone> phones = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			phones.add(ValueObjectAdapter.transform(TestDataCreator.createPhoneVO(), new MongoPhoneData()));
		}
		return phones;
	}

	@Test
	void test_addOrUpdatePhoneRecord_2() {
		Mockito.doAnswer(new Answer<Phone>() {

			@Override
			public Phone answer(InvocationOnMock invocation) throws Throwable {
				return new MongoPhoneData();
			}
		}).when(this.entityModelFactory).phone();
		// Mutating list
		List<Phone> phones = this.getRandomPhones(2);
		Mockito.when(this.userRepo.getPhoneNumbers(Mockito.anyString())).thenReturn(phones);
		PhoneNumberVO vo = TestDataCreator.createPhoneVO();
		Phone phn = ValueObjectAdapter.transform(vo, entityModelFactory.phone());
		this.humaneService.replaceAllPhoneRecords("testUser", List.of(vo));
		Mockito.verify(this.userRepo, Mockito.times(1)).replacePhoneNumbers("testUser", this.userContext.loggedInUser(),
				List.of(phn));
	}

	@Test
	void test_createOrUpdateAddress_1() {
		Mockito.doAnswer(new Answer<Address>() {

			@Override
			public Address answer(InvocationOnMock invocation) throws Throwable {
				return new MongoAddressData();
			}
		}).when(this.entityModelFactory).address();
		AddressVO add1 = TestDataCreator.createAddressVO();
		add1.setPrimary(true);
		List<Address> addresses = this.humaneService.createOrUpdateAddress(Collections.emptyList(), add1);
		Assertions.assertEquals(1, addresses.size());
		AddressVO add2 = TestDataCreator.createAddressVO();
		addresses = this.humaneService.createOrUpdateAddress(addresses, add2);
		Assertions.assertEquals(2, addresses.size());
		add1.setTag("HOME");
		addresses = this.humaneService.createOrUpdateAddress(addresses, add1);
		Assertions.assertEquals(2, addresses.size());
		Optional<Address> addMatch = addresses.stream().filter(add -> add.getId().equals(add1.getId())).findFirst();
		Assertions.assertTrue(addMatch.isPresent() && addMatch.get().getTag().equals("HOME"));
	}

	private List<Address> getRandomAddresses(Integer size) {
		List<Address> addresses = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			addresses.add(ValueObjectAdapter.transform(TestDataCreator.createAddressVO(), new MongoAddressData()));
		}
		return addresses;
	}

	@Test
	void test_createOrUpdateAddress_2() {
		Mockito.doAnswer(new Answer<Address>() {
			@Override
			public Address answer(InvocationOnMock invocation) throws Throwable {
				return new MongoAddressData();
			}
		}).when(this.entityModelFactory).address();
		// Mutating list
		List<Address> addresses = this.getRandomAddresses(2);
		Mockito.when(this.userRepo.getAddresses(Mockito.anyString())).thenReturn(addresses);
		AddressVO vo = TestDataCreator.createAddressVO();
		Address addrs = ValueObjectAdapter.transform(vo, entityModelFactory.address());
		this.humaneService.replaceAllAddress("testUser", List.of(vo));
		Mockito.verify(this.userRepo, Mockito.times(1)).replaceAddresses("testUser", this.userContext.loggedInUser(),
				List.of(addrs));
	}

	@Test
	void test_createRole() {
		HumaneRoleMasterVO vo = TestDataCreator.createRoleVO();
		Mockito.when(this.entityModelFactory.role()).thenReturn(new MongoRoleMasterEntity());
		Mockito.doAnswer(new Answer<HumaneRoleMaster>() {
			@Override
			public HumaneRoleMaster answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgument(0);
			}
		}).when(this.roleRepo).createRole(Mockito.any(HumaneRoleMaster.class));
		String roleId = this.humaneService.createRole(vo);
		Assertions.assertNotNull(roleId);
	}

	private List<HumaneRoleMaster> getRandomRoles(int size) {
		List<HumaneRoleMaster> roles = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			HumaneRoleMasterVO vo = TestDataCreator.createRoleVO();
			roles.add(ValueObjectAdapter.transform(vo, new MongoRoleMasterEntity()));
		}
		return roles;
	}

	@Test
	void test_findAllRoles() {
		Mockito.when(this.roleRepo.findAll()).thenReturn(this.getRandomRoles(5));
		List<HumaneRoleMasterVO> roles = this.humaneService.findAllRoles();
		Assertions.assertTrue(!roles.isEmpty() && roles.size() == 5);
	}

	@Test
	void test_updateRole() {
		Mockito.when(this.roleRepo.updateRole(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
				Mockito.anyString())).thenReturn(Mockito.mock(HumaneRoleMaster.class));
		Assertions.assertTrue(this.humaneService.updateRole(UniqueSequenceGenerator.CHAR12.next(), "DFDDFD", 1));
		Mockito.reset(this.roleRepo);
		Mockito.when(this.roleRepo.updateRole(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
				Mockito.anyString())).thenReturn(null);
		Assertions.assertFalse(this.humaneService.updateRole(UniqueSequenceGenerator.CHAR12.next(), "DFDDFD", 1));
	}

	@Test
	void test_toggleRoleDeprecation() {
		this.humaneService.toggleRoleDeprecation("R1", false);
		Mockito.verify(this.roleRepo, Mockito.times(1)).deprecate("R1", false, this.userContext.loggedInUser());
	}

	@Test
	void test_deleteRole() {
		Mockito.when(this.userRoleMapRepo.findAllMappingsByRole(Mockito.anyString(), Mockito.any(Pageable.class)))
				.thenReturn(List.of(Mockito.mock(HumaneUserRoleMap.class), Mockito.mock(HumaneUserRoleMap.class)));
		Assertions.assertFalse(this.humaneService.deleteRole("R1"));
		Mockito.reset(this.userRoleMapRepo);
		Mockito.when(this.userRoleMapRepo.findAllMappingsByRole(Mockito.anyString(), Mockito.any(Pageable.class)))
				.thenReturn(Collections.emptyList());
		Assertions.assertTrue(this.humaneService.deleteRole("R1"));
	}

	@Test
	void test_createUserRoleMap() throws HumaneEntityViolationException {
		HumaneUserRoleMap hurm = new MongoUserRoleMapEntity();
		Mockito.when(this.userRoleMapRepo.findByUserAndRoleId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(hurm));
		Assertions.assertThrows(HumaneEntityViolationException.class,
				() -> this.humaneService.createUserRoleMap("U1", "R1"));
		Mockito.reset(this.userRoleMapRepo);
		Mockito.when(this.entityModelFactory.userRole()).thenReturn(new MongoUserRoleMapEntity());
		Mockito.doAnswer(new Answer<HumaneUserRoleMap>() {

			@Override
			public HumaneUserRoleMap answer(InvocationOnMock invocation) throws Throwable {
				HumaneUserRoleMap map = invocation.getArgument(0);
				if (!StringUtils.hasText(map.getUserRoleId())) {
					map.setUserRoleId(UniqueSequenceGenerator.CHAR24.next());
				}
				return map;
			}
		}).when(this.userRoleMapRepo).create(Mockito.any(HumaneUserRoleMap.class));
		Mockito.when(this.userRoleMapRepo.findByUserAndRoleId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		Assertions.assertNotNull(this.humaneService.createUserRoleMap("U1", "R1"));
	}

	@Test
	void test_findMappingsByUser() {
		List<HumaneUserRoleMap> hurms = this.getRandomUserRoles("U1", null, 3);
		Mockito.when(this.userRoleMapRepo.findAllMappingsByUser(Mockito.anyString())).thenReturn(hurms);
		List<HumaneUserRoleMapVO> vos = this.humaneService.findMappingsByUser("U1");
		Assertions.assertEquals(3, vos.size());
		Mockito.reset(this.userRoleMapRepo);
		Mockito.when(this.userRoleMapRepo.findAllMappingsByUser(Mockito.anyString())).thenReturn(null);
		vos = this.humaneService.findMappingsByUser("U1");
		Assertions.assertEquals(0, vos.size());
	}

	@Test
	void test_findMappingsByRole() {
		List<HumaneUserRoleMap> hurms = this.getRandomUserRoles(null, "R1", 100);
		Mockito.when(this.userRoleMapRepo.findAllMappingsByRole(Mockito.anyString(), Mockito.any(Pageable.class)))
				.thenReturn(hurms);
		List<HumaneUserRoleMapVO> vos = this.humaneService.findMappingsByRole("R1", 0, 10);
		Assertions.assertEquals(100, vos.size());
		Mockito.reset(this.userRoleMapRepo);
		Mockito.when(this.userRoleMapRepo.findAllMappingsByRole(Mockito.anyString(), Mockito.any(Pageable.class)))
				.thenReturn(null);
		vos = this.humaneService.findMappingsByRole("R1", 0, 10);
		Assertions.assertEquals(0, vos.size());
	}

	private List<HumaneUserRoleMap> getRandomUserRoles(String userId, String roleId, int size) {
		List<HumaneUserRoleMap> roles = new ArrayList<>();
		while (size-- > 0) {
			HumaneUserRoleMap hurm = new MongoUserRoleMapEntity();
			if (StringUtils.hasText(userId)) {
				hurm.setUserId(userId);
			} else {
				hurm.setUserId(UniqueSequenceGenerator.CHAR16.next());
			}
			if (StringUtils.hasText(roleId)) {
				hurm.setRoleId(roleId);
			} else {
				hurm.setRoleId(UniqueSequenceGenerator.CHAR16.next());
			}
			roles.add(hurm);
			if (StringUtils.hasText(roleId) && StringUtils.hasText(userId)) {
				size = 0;
			}
		}
		return roles;
	}

	@Test
	void test_deleteRoleMapping() throws HumaneEntityViolationException {
		this.humaneService.deleteRoleMapping("U1R1");
		Mockito.verify(this.userRoleMapRepo, Mockito.times(1)).delete("U1R1");
		Mockito.reset(this.userRoleMapRepo);
		HumaneUserRoleMap hurm = new MongoUserRoleMapEntity();
		hurm.setUserRoleId("U1R1");
		Mockito.when(this.userRoleMapRepo.findByUserAndRoleId("U1", "R1")).thenReturn(Optional.of(hurm));
		this.humaneService.deleteRoleMapping("U1", "R1");
		Mockito.verify(this.userRoleMapRepo, Mockito.times(1)).delete("U1R1");
		Mockito.reset(this.userRoleMapRepo);
		Mockito.when(this.userRoleMapRepo.findByUserAndRoleId("U1", "R1")).thenReturn(Optional.empty());
		Assertions.assertThrows(HumaneEntityViolationException.class,
				() -> this.humaneService.deleteRoleMapping("U1", "R1"));
	}

}
