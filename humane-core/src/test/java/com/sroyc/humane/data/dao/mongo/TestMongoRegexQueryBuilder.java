package com.sroyc.humane.data.dao.mongo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestMongoRegexQueryBuilder {

	@Test
	void test() {
		MongoRegexQueryBuilder builder = MongoRegexQueryBuilder.newInstance();
		builder.add("email", "sab", false);
		builder.add("username", "sab", false);
		builder.add("fullname", "sab", false);
		String query = builder.buildORQuery();
		String expectedResult = "$or: [{ fullname: {$regex: '.*sab.*', $options : 'i'}},{ email: {$regex: '.*sab.*', $options : 'i'}},{ username: {$regex: '.*sab.*', $options : 'i'}}]";
		Assertions.assertNotNull(query);
		Assertions.assertEquals(expectedResult, query);
	}

}
