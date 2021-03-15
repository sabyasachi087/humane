package com.sroyc.humane.data.repo.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sroyc.humane.data.model.mongo.MongoUserMasterEntity;

@Repository
public interface MongoUserMasterRepository extends MongoRepository<MongoUserMasterEntity, String> {

}
