package com.sroyc.humane.view.models;

import com.sroyc.humane.data.model.HumaneRoleMaster;

public class HumaneRoleMasterVO extends AuditMarkerVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4887465659249269481L;

	private String roleId;
	private String roleCode;
	private String roleName;
	private Integer hierarchy;
	private boolean deprecate;

	public HumaneRoleMasterVO(HumaneRoleMaster role) {
		super(role);
		this.roleId = role.getRoleId();
		this.roleCode = role.getRoleCode();
		this.roleName = role.getRoleName();
		this.hierarchy = role.getHierarchy();
		this.deprecate = role.isDeprecate();
	}

	public HumaneRoleMasterVO() {
	}

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

}
