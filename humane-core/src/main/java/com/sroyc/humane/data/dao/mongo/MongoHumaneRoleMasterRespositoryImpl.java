package com.sroyc.humane.data.dao.mongo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sroyc.humane.data.dao.HumaneRoleMasterRespository;
import com.sroyc.humane.data.model.HumaneRoleMaster;
import com.sroyc.humane.data.model.mongo.MongoRoleMasterEntity;
import com.sroyc.humane.data.repo.mongo.MongoRoleMasterRepository;
import com.sroyc.humane.util.UniqueSequenceGenerator;

@Profile("sroyc.data.mongo")
@Component
public class MongoHumaneRoleMasterRespositoryImpl implements HumaneRoleMasterRespository {

	@SuppressWarnings("unused")
	private MongoTemplate mongoTemplate;
	private MongoRoleMasterRepository rmRepo;

	@Autowired
	public MongoHumaneRoleMasterRespositoryImpl(MongoTemplate mongoTemplate, MongoRoleMasterRepository rmRepo) {
		this.mongoTemplate = mongoTemplate;
		this.rmRepo = rmRepo;
	}

	@Override
	public HumaneRoleMaster createRole(HumaneRoleMaster role) {
		if (!StringUtils.hasText(role.getRoleId())) {
			role.setRoleId(UniqueSequenceGenerator.CHAR12.next());
		}
		return this.rmRepo.save((MongoRoleMasterEntity) role);
	}

	@Override
	public HumaneRoleMaster updateRole(String roleId, String roleName,Integer hierarchy, String updatedBy) {
		Optional<MongoRoleMasterEntity> entity = this.rmRepo.findById(roleId);
		if (entity.isPresent()) {
			MongoRoleMasterEntity role = entity.get();
			role.setRoleName(roleName);
			role.setHierarchy(hierarchy);
			role.setModifiedOn(LocalDateTime.now());
			role.setModifiedBy(updatedBy);
			return this.rmRepo.save(role);
		}
		return null;
	}

	@Override
	public void delete(String roleId) {
		this.rmRepo.deleteById(roleId);
	}

	@Override
	public void deprecate(String roleId, boolean isDeprecated, String updatedBy) {
		Optional<MongoRoleMasterEntity> entity = this.rmRepo.findById(roleId);
		if (entity.isPresent()) {
			MongoRoleMasterEntity role = entity.get();
			role.setDeprecate(isDeprecated);
			role.setModifiedBy(updatedBy);
			role.setModifiedOn(LocalDateTime.now());
			this.rmRepo.save(role);
		}
	}

	@Override
	public List<HumaneRoleMaster> findAll() {
		List<MongoRoleMasterEntity> entities = this.rmRepo.findAll();
		if (!CollectionUtils.isEmpty(entities)) {
			return entities.stream().map(entity -> (HumaneRoleMaster) entity).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public Optional<HumaneRoleMaster> findById(String roleId) {
		Optional<MongoRoleMasterEntity> entity = this.rmRepo.findById(roleId);
		if (entity.isPresent()) {
			return Optional.of(entity.get());
		}
		return Optional.empty();
	}

	@Override
	public Optional<HumaneRoleMaster> findByRoleCode(String roleCode) {
		Optional<MongoRoleMasterEntity> entity = this.rmRepo.findByRoleCode(roleCode);
		if (entity.isPresent()) {
			return Optional.of(entity.get());
		}
		return Optional.empty();
	}

}
