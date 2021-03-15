package com.sroyc.humane.view.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class HumaneUserDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9133513328095087432L;

	private HumaneUserMasterVO user;
	private List<HumaneRoleMasterVO> roles = Collections.emptyList();
	private String secret;

	public HumaneUserMasterVO getUser() {
		return user;
	}

	public void setUser(HumaneUserMasterVO user) {
		this.user = user;
	}

	public List<HumaneRoleMasterVO> getRoles() {
		return roles;
	}

	public void setRoles(List<HumaneRoleMasterVO> roles) {
		this.roles = roles;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
