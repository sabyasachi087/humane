package com.sroyc.humane.services;

import java.util.List;

import org.springframework.lang.Nullable;

import com.sroyc.humane.data.dao.HumaneUserMasterFilter;
import com.sroyc.humane.exception.HumaneEntityViolationException;
import com.sroyc.humane.view.models.AddressVO;
import com.sroyc.humane.view.models.HumaneRoleMasterVO;
import com.sroyc.humane.view.models.HumaneUserMasterVO;
import com.sroyc.humane.view.models.HumaneUserRoleMapVO;
import com.sroyc.humane.view.models.HumaneUserRoleView;
import com.sroyc.humane.view.models.PhoneNumberVO;

public interface HumaneService {

	HumaneUserMasterVO findByEmailOrUsername(String emailOrUsername);

	HumaneUserMasterVO findByUserId(String userId);

	List<HumaneUserMasterVO> findAllWithFilter(HumaneUserMasterFilter filter, Integer page, Integer pageSize);

	String createUser(HumaneUserMasterVO vo);

	boolean updateUser(HumaneUserMasterVO vo);

	boolean toggleDeletion(String userId, boolean isDelete);

	boolean toggleActivation(String userId, boolean isActive);

	String createRole(HumaneRoleMasterVO vo);

	boolean updateRole(String roleId, String rolename, @Nullable Integer hierarchy);

	void toggleRoleDeprecation(String roleId, boolean deprecate);

	/**
	 * Deletes a role if not being used .
	 * 
	 * @return true if success, false otherwise
	 */
	boolean deleteRole(String roleId);

	List<HumaneRoleMasterVO> findAllRoles();

	/**
	 * Creates user and role mapping.
	 * 
	 * @return Mapping Id
	 * @throws HumaneEntityViolationException if the mapping exists
	 */
	String createUserRoleMap(String userId, String roleId) throws HumaneEntityViolationException;

	List<HumaneUserRoleMapVO> findMappingsByUser(String userId);

	List<HumaneUserRoleMapVO> findMappingsByRole(String roleId, Integer page, Integer pageSize);

	void deleteRoleMapping(String userRoleId);

	/**
	 * Delete mapping for given user and role Id
	 * 
	 * @throws HumaneEntityViolationException if mapping not found
	 */
	void deleteRoleMapping(String userId, String roleId) throws HumaneEntityViolationException;

	List<PhoneNumberVO> getPhoneNumbers(String userId);

	List<AddressVO> getAddresses(String userId);

	void replaceAllPhoneRecords(String userId, List<PhoneNumberVO> phones);

	void replaceAllAddress(String userId, List<AddressVO> addresses);

	List<HumaneUserRoleView> findMappingViewOfUser(String userId);

}
