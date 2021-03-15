package com.sroyc.humane.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCommonUtils {

	@Test
	void test_fullname() {
		String firstName = "f1";
		String lastName = "l1";
		String middleName = "m1";
		String fullname = CommonUtils.fullname(firstName, lastName, middleName);
		Assertions.assertEquals("f1 m1 l1", fullname);
		firstName = null;
		lastName = null;
		fullname = CommonUtils.fullname(firstName, lastName, middleName);
		Assertions.assertEquals("m1", fullname);
		lastName = "l1";
		middleName = null;
		fullname = CommonUtils.fullname(firstName, lastName, middleName);
		Assertions.assertEquals("l1", fullname);
		lastName = null;
		fullname = CommonUtils.fullname(firstName, lastName, middleName);
		Assertions.assertEquals("", fullname);
	}

}
