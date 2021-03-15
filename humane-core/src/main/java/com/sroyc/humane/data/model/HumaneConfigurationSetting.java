package com.sroyc.humane.data.model;

import java.io.Serializable;

public interface HumaneConfigurationSetting extends AuditMarker {

	void setType(SettingType type);

	/** Must be unique */
	SettingType getType();

	void setSetting(Serializable setting);

	/** Setting in JSON format */
	Serializable getSettting();

	enum Attribute {
		USERNAME, EMAIL, FIRST_NAME, LAST_NAME;
	}

	enum SettingKey {
		DEFAULT_ROLE, AUTO_CREATE_USER;
	}

	enum SettingType {
		USER_ATTRIBUTES, HUMANE_SETTINGS
	}

}
