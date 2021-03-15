package com.sroyc.humane.data.dao.mongo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sroyc.humane.data.dao.HumaneUserMasterFilter;
import com.sroyc.humane.data.dao.HumaneUserMasterRepository;
import com.sroyc.humane.data.model.Address;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.Phone;
import com.sroyc.humane.data.model.mongo.MongoUserMasterEntity;
import com.sroyc.humane.data.repo.mongo.MongoUserMasterRepository;
import com.sroyc.humane.util.UniqueSequenceGenerator;

@Profile("sroyc.data.mongo")
@Component
public class MongoHumaneUserMasterRepositoryImpl implements HumaneUserMasterRepository {

	private MongoTemplate mongoTemplate;
	private MongoUserMasterRepository userRepo;

	@Autowired
	public MongoHumaneUserMasterRepositoryImpl(MongoTemplate mongoTemplate, MongoUserMasterRepository userRepo) {
		this.mongoTemplate = mongoTemplate;
		this.userRepo = userRepo;
	}

	@Override
	public HumaneUserMaster createUser(HumaneUserMaster hum) {
		MongoUserMasterEntity entity = (MongoUserMasterEntity) hum;
		if (!StringUtils.hasText(entity.getUserId())) {
			entity.setUserId(UniqueSequenceGenerator.CHAR16.next());
		}
		return this.userRepo.save(entity);
	}

	@Override
	public HumaneUserMaster updateUser(HumaneUserMaster hum) {
		return this.createUser(hum);
	}

	@Override
	public HumaneUserMaster toggleActivation(String userId, boolean activation, String modifiedBy) {
		Optional<MongoUserMasterEntity> entity = this.userRepo.findById(userId);
		if (entity.isPresent()) {
			MongoUserMasterEntity user = entity.get();
			user.setActive(activation);
			user.setModifiedOn(LocalDateTime.now());
			user.setModifiedBy(modifiedBy);
			return this.userRepo.save(user);
		}
		return null;
	}

	@Override
	public HumaneUserMaster toggleDeletion(String userId, boolean deletion, String modifiedBy) {
		Optional<MongoUserMasterEntity> entity = this.userRepo.findById(userId);
		if (entity.isPresent()) {
			MongoUserMasterEntity user = entity.get();
			user.setDelete(deletion);
			user.setModifiedOn(LocalDateTime.now());
			user.setModifiedBy(modifiedBy);
			return this.userRepo.save(user);
		}
		return null;
	}

	@Override
	public Optional<HumaneUserMaster> findById(String userId) {
		Optional<MongoUserMasterEntity> entity = this.userRepo.findById(userId);
		if (entity.isPresent()) {
			return Optional.of(entity.get());
		}
		return Optional.empty();
	}

	@Override
	public Optional<HumaneUserMaster> findByEmailOrUsername(String emailOrUsername) {
		Query q = new Query();
		q.addCriteria(
				Criteria.where("email").is(emailOrUsername).orOperator(Criteria.where("username").is(emailOrUsername)));
		MongoUserMasterEntity user = this.mongoTemplate.findOne(q, MongoUserMasterEntity.class);
		if (user != null) {
			return Optional.of(user);
		}
		return Optional.empty();
	}

	@Override
	public List<HumaneUserMaster> findAllWithFilters(HumaneUserMasterFilter filter, Pageable page) {
		BasicQuery query = new BasicQuery(this.buildFilterQuery(filter));
		List<MongoUserMasterEntity> entities = this.mongoTemplate.find(query.with(page), MongoUserMasterEntity.class);
		if (!CollectionUtils.isEmpty(entities)) {
			return entities.stream().map(entity -> (HumaneUserMaster) entity).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	protected String buildFilterQuery(HumaneUserMasterFilter filter) {
		StringBuilder sb = new StringBuilder("{ ");
		if (filter.getActivation() != null) {
			sb.append(" active : ").append(filter.getActivation()).append(",");
		}
		if (filter.getDeletion() != null) {
			sb.append(" delete : ").append(filter.getDeletion()).append(",");
		}
		if (StringUtils.hasText(filter.getName())) {
			MongoRegexQueryBuilder builder = MongoRegexQueryBuilder.newInstance();
			builder.add("email", filter.getName(), false).add("username", filter.getName(), false).add("fullname",
					filter.getName(), false);
			sb.append(builder.buildORQuery());
		}
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.append(" }").toString();
	}

	@Override
	public List<Phone> replacePhoneNumbers(String userId, String modifiedBy, List<? extends Phone> phoneNumbers) {
		Optional<MongoUserMasterEntity> entity = this.userRepo.findById(userId);
		if (entity.isPresent()) {
			MongoUserMasterEntity user = entity.get();
			user.setPhoneNumbers(phoneNumbers);
			user.setModifiedBy(modifiedBy);
			user.setModifiedOn(LocalDateTime.now());
			user = this.userRepo.save(user);
			return user.getPhoneNumbers();
		}
		return Collections.emptyList();
	}

	@Override
	public List<Address> replaceAddresses(String userId, String modifiedBy, List<? extends Address> addresses) {
		Optional<MongoUserMasterEntity> entity = this.userRepo.findById(userId);
		if (entity.isPresent()) {
			MongoUserMasterEntity user = entity.get();
			user.setAddresses(addresses);
			user.setModifiedBy(modifiedBy);
			user.setModifiedOn(LocalDateTime.now());
			user = this.userRepo.save(user);
			return user.getAddresses();
		}
		return Collections.emptyList();
	}

	@Override
	public List<Phone> getPhoneNumbers(String userId) {
		Optional<MongoUserMasterEntity> entity = this.userRepo.findById(userId);
		if (entity.isPresent()) {
			return entity.get().getPhoneNumbers();
		}
		return Collections.emptyList();
	}

	@Override
	public List<Address> getAddresses(String userId) {
		Optional<MongoUserMasterEntity> entity = this.userRepo.findById(userId);
		if (entity.isPresent()) {
			return entity.get().getAddresses();
		}
		return Collections.emptyList();
	}

}
