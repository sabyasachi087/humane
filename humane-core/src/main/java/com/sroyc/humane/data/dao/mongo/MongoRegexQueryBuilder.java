package com.sroyc.humane.data.dao.mongo;

import java.util.HashMap;
import java.util.Map;

public class MongoRegexQueryBuilder {

	private static final String LIKE_REGEX = ".*%s.*";
	private static final String REGEX_QUERY = "{ %s: {$regex: '%s', $options : 'i'}}";
	private Map<String, String> fieldValue = new HashMap<>();

	private MongoRegexQueryBuilder() {
	}

	public static final MongoRegexQueryBuilder newInstance() {
		return new MongoRegexQueryBuilder();
	}

	public MongoRegexQueryBuilder add(String fieldName, String value, boolean withRegex) {
		if (withRegex) {
			this.fieldValue.put(fieldName, value);
		} else {
			this.fieldValue.put(fieldName, String.format(LIKE_REGEX, value));
		}
		return this;
	}

	/**
	 * Builds a or query from internal map of key value. Once this method is
	 * invoked, internal map will be flushed
	 */
	public String buildORQuery() {
		final StringBuilder builder = new StringBuilder("$or: [");
		fieldValue.entrySet().forEach(
				entry -> builder.append(String.format(REGEX_QUERY, entry.getKey(), entry.getValue())).append(","));
		if (builder.charAt(builder.length() - 1) == ',') {
			builder.deleteCharAt(builder.length() - 1);
		}
		builder.append("]");
		fieldValue.clear();
		return builder.toString();
	}

}
