package com.sroyc.humane.services;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.sroyc.humane.util.UniqueSequenceGenerator;
import com.sroyc.humane.view.models.AddressVO;
import com.sroyc.humane.view.models.HumaneRoleMasterVO;
import com.sroyc.humane.view.models.HumaneUserMasterVO;
import com.sroyc.humane.view.models.PhoneNumberVO;

public class TestDataCreator {

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private TestDataCreator() {
	}

	public static final HumaneUserMasterVO createUserVO(String email) {
		HumaneUserMasterVO vo = new HumaneUserMasterVO();
		vo.setActive(true);
		vo.setEmail(email);
		vo.setUsername(email);
		vo.setFirstName(RandomStringUtils.randomAlphabetic(10));
		vo.setLastName(RandomStringUtils.randomAlphabetic(10));
		return vo;
	}

	public static final PhoneNumberVO createPhoneVO() {
		PhoneNumberVO vo = new PhoneNumberVO();
		vo.setPhoneNumber(RandomStringUtils.randomNumeric(10));
		vo.setCountryCode("001");
		vo.setPrimary(false);
		return vo;
	}

	public static final AddressVO createAddressVO() {
		AddressVO vo = new AddressVO();
		vo.setCity(RandomStringUtils.randomAlphabetic(4, 10));
		vo.setCodeOrPin(RandomStringUtils.randomNumeric(7));
		vo.setLine1(RandomStringUtils.randomAlphabetic(20, 70));
		vo.setLine1(RandomStringUtils.randomAlphabetic(0, 50));
		vo.setPrimary(false);
		vo.setState(RandomStringUtils.randomAlphabetic(7, 20));
		vo.setId(UniqueSequenceGenerator.CHAR12.next());
		vo.setTag(RandomStringUtils.random(RANDOM.nextInt(10) + 5));
		return vo;
	}

	public static final HumaneRoleMasterVO createRoleVO() {
		HumaneRoleMasterVO vo = new HumaneRoleMasterVO();
		vo.setRoleCode(RandomStringUtils.randomAlphabetic(5));
		vo.setHierarchy(1);
		vo.setRoleId(UniqueSequenceGenerator.CHAR16.next());
		vo.setRoleName(RandomStringUtils.randomAlphabetic(50));
		return vo;
	}

}
