package com.sroyc.humane.data.model.mongo;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sroyc.humane.data.model.HumaneRoleMaster;

@Profile("sroyc.data.mongo")
@Document(collection = "role_master")
public class MongoRoleMasterEntity implements HumaneRoleMaster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7917427047286745253L;

	@Id
	private String roleId;
	@Indexed(unique = true, name = "role_mst_code_uk")
	private String roleCode;
	private String roleName;
	private Integer hierarchy;
	private boolean deprecate;
	private String modifiedBy;
	private LocalDateTime modifiedOn;
	private String createdBy;
	private LocalDateTime createdOn;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}

	public boolean isDeprecate() {
		return deprecate;
	}

	public void setDeprecate(boolean deprecate) {
		this.deprecate = deprecate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
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
