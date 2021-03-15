package com.sroyc.humane.data.dao.mongo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.sroyc.humane.data.dao.HumaneConfigurationSettingRepository;
import com.sroyc.humane.data.model.HumaneConfigurationSetting;
import com.sroyc.humane.data.model.HumaneConfigurationSetting.Attribute;
import com.sroyc.humane.data.model.HumaneConfigurationSetting.SettingKey;
import com.sroyc.humane.data.model.HumaneConfigurationSetting.SettingType;
import com.sroyc.humane.data.model.mongo.MongoConfigurationSettingEntity;
import com.sroyc.humane.data.repo.mongo.MongoConfigurationSettingRepository;
import com.sroyc.humane.util.UniqueSequenceGenerator;

@Profile("sroyc.data.mongo")
@Component
public class MongoHumaneConfigurationSettingRepositoryImpl implements HumaneConfigurationSettingRepository {

	@SuppressWarnings("unused")
	private MongoTemplate mongoTemplate;
	private MongoConfigurationSettingRepository configRepo;

	@Autowired
	public MongoHumaneConfigurationSettingRepositoryImpl(MongoTemplate mongoTemplate,
			MongoConfigurationSettingRepository configRepo) {
		this.mongoTemplate = mongoTemplate;
		this.configRepo = configRepo;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<Attribute, String> getUserAttributes() {
		Optional<MongoConfigurationSettingEntity> entity = this.configRepo
				.findByType(HumaneConfigurationSetting.SettingType.USER_ATTRIBUTES);
		if (entity.isPresent()) {
			return (Map) entity.get().getSettting();
		}
		return Collections.emptyMap();
	}

	@Override
	public String saveUserAttributes(Map<Attribute, String> userAttributes, String proposedBy) {
		MongoConfigurationSettingEntity config = this
				.getOrCreateEntity(HumaneConfigurationSetting.SettingType.USER_ATTRIBUTES, proposedBy);
		config.setSetting((Serializable) userAttributes);
		config = this.configRepo.save(config);
		return config.getId();
	}

	protected MongoConfigurationSettingEntity getOrCreateEntity(SettingType type, String proposedBy) {
		Optional<MongoConfigurationSettingEntity> entity = this.configRepo.findByType(type);
		MongoConfigurationSettingEntity config = null;
		if (entity.isPresent()) {
			config = entity.get();
			config.setModifiedBy(proposedBy);
			config.setModifiedOn(LocalDateTime.now());
		} else {
			config = new MongoConfigurationSettingEntity();
			config.setCreatedBy(proposedBy);
			config.setCreatedOn(LocalDateTime.now());
			config.setId(UniqueSequenceGenerator.CHAR12.next());
			config.setType(type);
		}
		return config;
	}

	@Override
	public void delete(SettingType type) {
		Optional<MongoConfigurationSettingEntity> entity = this.configRepo.findByType(type);
		if (entity.isPresent()) {
			this.configRepo.delete(entity.get());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<SettingKey, String> getHumaneSettings() {
		Optional<MongoConfigurationSettingEntity> entity = this.configRepo
				.findByType(HumaneConfigurationSetting.SettingType.HUMANE_SETTINGS);
		if (entity.isPresent()) {
			return (Map) entity.get().getSettting();
		}
		return Collections.emptyMap();
	}

	@Override
	public String saveHumaneSettings(Map<SettingKey, String> humaneSettings, String proposedBy) {
		MongoConfigurationSettingEntity config = this
				.getOrCreateEntity(HumaneConfigurationSetting.SettingType.HUMANE_SETTINGS, proposedBy);
		config.setSetting((Serializable) humaneSettings);
		config = this.configRepo.save(config);
		return config.getId();
	}

}
