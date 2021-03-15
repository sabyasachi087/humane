package com.sroyc.humane.data.model;

public interface HumaneRoleMaster extends AuditMarker {

	public String getRoleId();

	public void setRoleId(String roleId);

	public String getRoleCode();

	public void setRoleCode(String roleCode);

	public String getRoleName();

	public void setRoleName(String roleName);

	public Integer getHierarchy();

	public void setHierarchy(Integer hierarchy);

	public boolean isDeprecate();

	public void setDeprecate(boolean deprecate);

}
