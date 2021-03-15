package com.sroyc.humane.data.repo.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sroyc.humane.data.model.mongo.MongoRoleMasterEntity;

@Repository
public interface MongoRoleMasterRepository extends MongoRepository<MongoRoleMasterEntity, String> {
	
	public Optional<MongoRoleMasterEntity> findByRoleCode(String roleCode);

}
