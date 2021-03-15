package com.sroyc.humane.api.v1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HumaneConfgurableProperty {

	@Value("${humane.user.list.page.size:20}")
	private Integer userListPageSize;

	@Value("${humane.user.role.map.list.page.size:20}")
	private Integer userRoleMapListPageSize;

	public Integer getUserListPageSize() {
		return userListPageSize;
	}

	public Integer getUserRoleMapListPageSize() {
		return userRoleMapListPageSize;
	}

}
