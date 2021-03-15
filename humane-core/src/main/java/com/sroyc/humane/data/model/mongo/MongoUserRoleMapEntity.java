package com.sroyc.humane.data.model.mongo;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sroyc.humane.data.model.HumaneUserRoleMap;

@Profile("sroyc.data.mongo")
@CompoundIndex(name = "user_role_ui", def = "{'userId':1,'roleId':1}, {'unique':true}")
@Document(collection = "user_role_map")
public class MongoUserRoleMapEntity implements HumaneUserRoleMap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1798449508807595856L;

	@Id
	private String userRoleId;
	private String userId;
	private String roleId;
	private String createdBy;
	private LocalDateTime createdOn;

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

}
