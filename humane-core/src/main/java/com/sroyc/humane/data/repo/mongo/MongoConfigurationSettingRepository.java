package com.sroyc.humane.data.repo.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sroyc.humane.data.model.HumaneConfigurationSetting.SettingType;
import com.sroyc.humane.data.model.mongo.MongoConfigurationSettingEntity;

@Repository
public interface MongoConfigurationSettingRepository extends MongoRepository<MongoConfigurationSettingEntity, String> {
	
	Optional<MongoConfigurationSettingEntity> findByType(SettingType type);

}