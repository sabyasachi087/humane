package com.sroyc.humane.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class CommonUtils {

	private CommonUtils() {
	}

	public static final String fullname(String firstname, String lastname, @Nullable String middlename) {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.hasText(firstname) ? firstname : "");
		if (StringUtils.hasText(middlename)) {
			if (sb.length() > 0) {
				sb.append(HumaneGeneralConstant.SPACE_STR);
			}
			sb.append(middlename);
		}
		if (StringUtils.hasText(lastname)) {
			if (sb.length() > 0) {
				sb.append(HumaneGeneralConstant.SPACE_STR);
			}
			sb.append(lastname);
		}
		return sb.toString();
	}

	public static final Map<String, String> toStringAttributes(Map<String, Object> attributes) {
		Map<String, String> strAttr = new HashMap<>();
		if (!CollectionUtils.isEmpty(attributes)) {
			attributes.entrySet().forEach(record -> {
				if (record.getValue() instanceof String) {
					strAttr.put(record.getKey(), (String) record.getValue());
				}
			});
		}
		return strAttr;
	}
}
