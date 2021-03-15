package com.sroyc.humane.data.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.Nullable;

import com.sroyc.humane.data.model.HumaneRoleMaster;

public interface HumaneRoleMasterRespository {

	public HumaneRoleMaster createRole(HumaneRoleMaster role);

	public List<HumaneRoleMaster> findAll();

	public Optional<HumaneRoleMaster> findById(String roleId);

	public Optional<HumaneRoleMaster> findByRoleCode(String roleCode);

	public HumaneRoleMaster updateRole(String roleId, String roleName, @Nullable Integer hierarchy, String updatedBy);

	public void delete(String roleId);

	public void deprecate(String roleId, boolean isDeprecated, String updatedBy);

}
