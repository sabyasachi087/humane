package com.sroyc.humane.data.model.mongo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sroyc.humane.data.model.HumaneConfigurationSetting;

@Profile("sroyc.data.mongo")
@Document(collection = "humane_config_setting")
public class MongoConfigurationSettingEntity implements HumaneConfigurationSetting {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5617670804579692736L;

	@Id
	private String id;
	@Indexed(unique = true)
	private SettingType type;
	private Map<Attribute, String> setting;
	private String modifiedBy;
	private LocalDateTime modifiedOn;
	private String createdBy;
	private LocalDateTime createdOn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setType(SettingType type) {
		this.type = type;
	}

	@Override
	public SettingType getType() {
		return this.type;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setSetting(Serializable setting) {
		this.setting = (Map) setting;
	}

	@Override
	public Serializable getSettting() {
		return (Serializable) this.setting;
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
