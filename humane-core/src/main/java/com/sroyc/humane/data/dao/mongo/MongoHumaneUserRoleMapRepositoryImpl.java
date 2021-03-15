package com.sroyc.humane.data.dao.mongo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sroyc.humane.data.dao.HumaneUserRoleMapRepository;
import com.sroyc.humane.data.model.HumaneUserRoleMap;
import com.sroyc.humane.data.model.mongo.MongoUserRoleMapEntity;
import com.sroyc.humane.data.repo.mongo.MongoUserRoleMapRepository;
import com.sroyc.humane.util.UniqueSequenceGenerator;
import com.sroyc.humane.view.models.HumaneUserRoleView;

@Profile("sroyc.data.mongo")
@Component
public class MongoHumaneUserRoleMapRepositoryImpl implements HumaneUserRoleMapRepository {

	private MongoTemplate mongoTemplate;
	private MongoUserRoleMapRepository userRoleRepo;

	@Autowired
	public MongoHumaneUserRoleMapRepositoryImpl(MongoTemplate mongoTemplate, MongoUserRoleMapRepository userRoleRepo) {
		super();
		this.mongoTemplate = mongoTemplate;
		this.userRoleRepo = userRoleRepo;
	}

	@Override
	public HumaneUserRoleMap create(HumaneUserRoleMap roleMap) {
		MongoUserRoleMapEntity entity = (MongoUserRoleMapEntity) roleMap;
		if (!StringUtils.hasText(entity.getUserRoleId())) {
			entity.setUserRoleId(UniqueSequenceGenerator.CHAR24.next());
		}
		return this.userRoleRepo.save(entity);
	}

	@Override
	public Optional<HumaneUserRoleMap> findByUserAndRoleId(String userId, String roleId) {
		Query q = new Query();
		q.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("roleId").is(roleId)));
		MongoUserRoleMapEntity entity = this.mongoTemplate.findOne(q, MongoUserRoleMapEntity.class);
		if (entity != null) {
			return Optional.<HumaneUserRoleMap>of(entity);
		}
		return Optional.empty();
	}

	@Override
	public void delete(String userRoleId) {
		this.userRoleRepo.deleteById(userRoleId);
	}

	@Override
	public List<HumaneUserRoleMap> findAllMappingsByUser(String userId) {
		Query q = new Query();
		q.addCriteria(Criteria.where("userId").is(userId));
		List<MongoUserRoleMapEntity> entities = this.mongoTemplate.find(q, MongoUserRoleMapEntity.class);
		if (!CollectionUtils.isEmpty(entities)) {
			return entities.stream().map(entity -> (HumaneUserRoleMap) entity).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<HumaneUserRoleMap> findAllMappingsByRole(String roleId, Pageable page) {
		Query q = new Query();
		q.addCriteria(Criteria.where("roleId").is(roleId));
		List<MongoUserRoleMapEntity> entities = this.mongoTemplate.find(q.with(page), MongoUserRoleMapEntity.class);
		if (!CollectionUtils.isEmpty(entities)) {
			return entities.stream().map(entity -> (HumaneUserRoleMap) entity).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public void deleteAllMappings(String userId) {
		Query q = new Query();
		q.addCriteria(Criteria.where("userId").is(userId));
		this.mongoTemplate.findAllAndRemove(q, MongoUserRoleMapEntity.class);
	}

	@Override
	public List<HumaneUserRoleView> findMappingViewOfUser(String userId) {
		MatchOperation matchOps = Aggregation.match(new Criteria("userId").is(userId));
		LookupOperation lookupOps = Aggregation.lookup("role_master", "roleId", Fields.UNDERSCORE_ID, "role");
		ProjectionOperation projectOp1 = Aggregation.project("userId", "roleId")
				.and(ArrayOperators.ArrayElemAt.arrayOf("role").elementAt(0)).as("role");
		ProjectionOperation projectOp2 = Aggregation.project("userId", "roleId").and(Fields.UNDERSCORE_ID)
				.as("userRoleId").and("role.roleCode").as("roleCode").and("role.roleName").as("roleName")
				.andExclude(Fields.UNDERSCORE_ID);
		Aggregation agg = Aggregation.newAggregation(matchOps, lookupOps, projectOp1, projectOp2);
		AggregationResults<HumaneUserRoleView> results = mongoTemplate.aggregate(agg, "user_role_map",
				HumaneUserRoleView.class);
		return results.getMappedResults();
	}

}
