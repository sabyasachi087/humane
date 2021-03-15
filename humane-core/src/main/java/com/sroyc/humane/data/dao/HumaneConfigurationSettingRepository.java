package com.sroyc.humane.data.dao;

import java.util.Map;

import com.sroyc.humane.data.model.HumaneConfigurationSetting;
import com.sroyc.humane.data.model.HumaneConfigurationSetting.SettingType;

public interface HumaneConfigurationSettingRepository {

	Map<HumaneConfigurationSetting.Attribute, String> getUserAttributes();

	Map<HumaneConfigurationSetting.SettingKey, String> getHumaneSettings();

	/**
	 * Caters to creation or updation
	 * 
	 * @return Id of entity
	 */
	String saveUserAttributes(Map<HumaneConfigurationSetting.Attribute, String> userAttributes, String proposedBy);

	/**
	 * Caters to creation or updation.
	 * 
	 * @return Id of entity
	 */
	String saveHumaneSettings(Map<HumaneConfigurationSetting.SettingKey, String> humaneSettings, String proposedBy);

	void delete(SettingType type);

}
