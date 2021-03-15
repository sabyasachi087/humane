package com.sroyc.humane.data.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.sroyc.humane.data.model.HumaneUserRoleMap;
import com.sroyc.humane.view.models.HumaneUserRoleView;

public interface HumaneUserRoleMapRepository {

	public HumaneUserRoleMap create(HumaneUserRoleMap roleMap);

	public void delete(String userRoleId);

	List<HumaneUserRoleMap> findAllMappingsByUser(String userId);
	
	List<HumaneUserRoleView> findMappingViewOfUser(String userId);

	List<HumaneUserRoleMap> findAllMappingsByRole(String roleId, Pageable page);

	void deleteAllMappings(String userId);

	/** Find unique object for given user and role id */
	Optional<HumaneUserRoleMap> findByUserAndRoleId(String userId, String roleId);

	

}
