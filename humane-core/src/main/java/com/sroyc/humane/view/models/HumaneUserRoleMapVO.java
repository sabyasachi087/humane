package com.sroyc.humane.view.models;

import com.sroyc.humane.data.model.HumaneUserRoleMap;

public class HumaneUserRoleMapVO extends CreationAuditAttributesVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6007598460292502131L;

	private String userRoleId;
	private String userId;
	private String roleId;

	public HumaneUserRoleMapVO() {
		super();
	}

	public HumaneUserRoleMapVO(HumaneUserRoleMap hurm) {
		super(hurm);
		this.userId = hurm.getUserId();
		this.userRoleId = hurm.getUserRoleId();
		this.roleId = hurm.getRoleId();
	}

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

}
