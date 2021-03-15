package com.sroyc.humane.data.model;

public interface EntityDataModelFactory {

	public Address address();

	public Phone phone();

	public HumaneRoleMaster role();

	public HumaneUserMaster user();

	public HumaneUserRoleMap userRole();

	public HumaneConfigurationSetting configSetting();

}
