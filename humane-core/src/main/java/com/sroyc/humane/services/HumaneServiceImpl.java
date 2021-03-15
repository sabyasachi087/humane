package com.sroyc.humane.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sroyc.humane.data.dao.HumaneRoleMasterRespository;
import com.sroyc.humane.data.dao.HumaneUserMasterFilter;
import com.sroyc.humane.data.dao.HumaneUserMasterRepository;
import com.sroyc.humane.data.dao.HumaneUserRoleMapRepository;
import com.sroyc.humane.data.model.Address;
import com.sroyc.humane.data.model.EntityDataModelFactory;
import com.sroyc.humane.data.model.HumaneRoleMaster;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.HumaneUserRoleMap;
import com.sroyc.humane.data.model.Phone;
import com.sroyc.humane.exception.HumaneEntityViolationException;
import com.sroyc.humane.util.CommonUtils;
import com.sroyc.humane.util.UniqueSequenceGenerator;
import com.sroyc.humane.util.ValueObjectAdapter;
import com.sroyc.humane.view.models.AddressVO;
import com.sroyc.humane.view.models.HumaneRoleMasterVO;
import com.sroyc.humane.view.models.HumaneUserMasterVO;
import com.sroyc.humane.view.models.HumaneUserRoleMapVO;
import com.sroyc.humane.view.models.HumaneUserRoleView;
import com.sroyc.humane.view.models.PhoneNumberVO;

@Service
public class HumaneServiceImpl implements HumaneService {

	@Autowired
	private HumaneUserMasterRepository userRepo;
	@Autowired
	private HumaneUserRoleMapRepository userRoleMapRepo;
	@Autowired
	private HumaneRoleMasterRespository roleRepo;
	@Autowired
	private HumaneUserContext userContext;
	@Autowired
	private EntityDataModelFactory entityModelFactory;

	@Override
	public HumaneUserMasterVO findByEmailOrUsername(String emailOrUsername) {
		Optional<HumaneUserMaster> user = this.userRepo.findByEmailOrUsername(emailOrUsername);
		if (user.isPresent()) {
			return new HumaneUserMasterVO(user.get());
		}
		return null;
	}

	@Override
	public HumaneUserMasterVO findByUserId(String userId) {
		Optional<HumaneUserMaster> user = this.userRepo.findById(userId);
		if (user.isPresent()) {
			return new HumaneUserMasterVO(user.get());
		}
		return null;
	}

	@Override
	public List<HumaneUserMasterVO> findAllWithFilter(HumaneUserMasterFilter filter, Integer page, Integer pageSize) {
		List<HumaneUserMaster> hums = this.userRepo.findAllWithFilters(filter,
				PageRequest.of(page, pageSize, Direction.ASC, "userId"));
		if (!CollectionUtils.isEmpty(hums)) {
			return hums.stream().map(HumaneUserMasterVO::new).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public String createUser(HumaneUserMasterVO vo) {
		HumaneUserMaster user = ValueObjectAdapter.transform(vo, entityModelFactory.user());
		if (!StringUtils.hasText(user.getUsername())) {
			user.setUsername(vo.getEmail());
		}
		user.setUserId(UniqueSequenceGenerator.CHAR16.next());
		user.setFullname(CommonUtils.fullname(user.getFirstName(), user.getLastName(), user.getMiddleName()));
		user.setCreatedBy(this.userContext.loggedInUser());
		user.setCreatedOn(LocalDateTime.now());
		user = this.userRepo.createUser(user);
		return user.getUserId();
	}

	@Override
	public boolean updateUser(HumaneUserMasterVO vo) {
		Optional<HumaneUserMaster> data = this.userRepo.findById(vo.getUserId());
		if (data.isPresent()) {
			HumaneUserMaster user = data.get();
			ValueObjectAdapter.update(vo, user);
			user.setFullname(CommonUtils.fullname(user.getFirstName(), user.getLastName(), user.getMiddleName()));
			user.setModifiedBy(this.userContext.loggedInUser());
			user.setModifiedOn(LocalDateTime.now());
			this.userRepo.updateUser(user);
			return true;
		}
		return false;
	}

	@Override
	public boolean toggleDeletion(String userId, boolean isDelete) {
		HumaneUserMaster user = this.userRepo.toggleDeletion(userId, isDelete, this.userContext.loggedInUser());
		return user != null;
	}

	@Override
	public boolean toggleActivation(String userId, boolean isActive) {
		HumaneUserMaster user = this.userRepo.toggleActivation(userId, isActive, this.userContext.loggedInUser());
		return user != null;
	}

	@Override
	public List<PhoneNumberVO> getPhoneNumbers(String userId) {
		List<Phone> userPhones = this.userRepo.getPhoneNumbers(userId);
		if (!CollectionUtils.isEmpty(userPhones)) {
			return userPhones.stream().map(PhoneNumberVO::new).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<AddressVO> getAddresses(String userId) {
		List<Address> useraddresses = this.userRepo.getAddresses(userId);
		if (!CollectionUtils.isEmpty(useraddresses)) {
			return useraddresses.stream().map(AddressVO::new).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public void replaceAllPhoneRecords(String userId, List<PhoneNumberVO> phones) {
		List<Phone> userPhones = new ArrayList<>();
		if (!CollectionUtils.isEmpty(phones)) {
			for (PhoneNumberVO phn : phones) {
				userPhones = this.addOrUpdatePhoneRecord(userPhones, phn);
			}
		}
		this.userRepo.replacePhoneNumbers(userId, this.userContext.loggedInUser(), userPhones);
	}

	protected List<Phone> addOrUpdatePhoneRecord(List<Phone> phones, PhoneNumberVO phoneVO) {
		if (CollectionUtils.isEmpty(phones)) {
			phones = new ArrayList<>();
			phones.add(ValueObjectAdapter.transform(phoneVO, entityModelFactory.phone()));
		} else {
			Optional<Phone> match = phones.stream().filter(phn -> phn.getPhoneNumber().equals(phoneVO.getPhoneNumber())
					&& phn.getCountryCode().equals(phoneVO.getCountryCode())).findAny();
			if (match.isEmpty()) {
				phones.add(ValueObjectAdapter.transform(phoneVO, entityModelFactory.phone()));
			} else {
				Phone phn = match.get();
				phn.setPrimary(phoneVO.isPrimary());
			}
		}
		return phones;
	}

	@Override
	public void replaceAllAddress(String userId, List<AddressVO> addresses) {
		List<Address> useraddresses = new ArrayList<>();
		if (!CollectionUtils.isEmpty(addresses)) {
			for (AddressVO addrs : addresses) {
				useraddresses = this.createOrUpdateAddress(useraddresses, addrs);
			}
		}
		this.userRepo.replaceAddresses(userId, this.userContext.loggedInUser(), useraddresses);
	}

	protected List<Address> createOrUpdateAddress(List<Address> addresses, AddressVO addrsVO) {
		if (CollectionUtils.isEmpty(addresses)) {
			addresses = new ArrayList<>();
		}
		Address addrs = ValueObjectAdapter.transform(addrsVO, entityModelFactory.address());
		if (!StringUtils.hasText(addrs.getId())) {
			addrs.setId(UniqueSequenceGenerator.CHAR16.next());
		} else {
			Optional<? extends Address> match = addresses.stream().filter(adr -> adr.getId().equals(addrsVO.getId()))
					.findAny();
			if (match.isPresent()) {
				addresses.remove(match.get());
			}
		}
		addresses.add(addrs);
		return addresses;
	}

	@Override
	public String createRole(HumaneRoleMasterVO vo) {
		Optional<HumaneRoleMaster> data = this.roleRepo.findByRoleCode(vo.getRoleCode());
		if (data.isPresent()) {
			return data.get().getRoleId();
		}
		HumaneRoleMaster role = ValueObjectAdapter.transform(vo, entityModelFactory.role());
		role.setRoleId(UniqueSequenceGenerator.CHAR12.next());
		role.setCreatedBy(this.userContext.loggedInUser());
		role.setCreatedOn(LocalDateTime.now());
		role = this.roleRepo.createRole(role);
		return role.getRoleId();
	}

	@Override
	public List<HumaneRoleMasterVO> findAllRoles() {
		return this.roleRepo.findAll().stream().map(HumaneRoleMasterVO::new).collect(Collectors.toList());
	}

	@Override
	public boolean updateRole(String roleId, String rolename, Integer hierarchy) {
		return this.roleRepo.updateRole(roleId, rolename, hierarchy, this.userContext.loggedInUser()) != null;
	}

	@Override
	public void toggleRoleDeprecation(String roleId, boolean deprecate) {
		this.roleRepo.deprecate(roleId, deprecate, this.userContext.loggedInUser());
	}

	@Override
	public boolean deleteRole(String roleId) {
		List<HumaneUserRoleMap> mappings = this.userRoleMapRepo.findAllMappingsByRole(roleId,
				PageRequest.of(0, 10, Direction.ASC, "userRoleId"));
		if (CollectionUtils.isEmpty(mappings)) {
			this.roleRepo.delete(roleId);
			return true;
		}
		return false;
	}

	@Override
	public String createUserRoleMap(String userId, String roleId) throws HumaneEntityViolationException {
		Optional<HumaneUserRoleMap> data = this.userRoleMapRepo.findByUserAndRoleId(userId, roleId);
		if (data.isEmpty()) {
			HumaneUserRoleMap map = this.generateUserRoleMap(userId, roleId);
			map = this.userRoleMapRepo.create(map);
			return map.getUserRoleId();
		}
		throw new HumaneEntityViolationException(
				"Unique constraing violation for userId [" + userId + "] and roleId [" + roleId + "]");
	}

	protected HumaneUserRoleMap generateUserRoleMap(String userId, String roleId) {
		HumaneUserRoleMap map = entityModelFactory.userRole();
		map.setCreatedBy(this.userContext.loggedInUser());
		map.setCreatedOn(LocalDateTime.now());
		map.setRoleId(roleId);
		map.setUserId(userId);
		map.setUserRoleId(UniqueSequenceGenerator.CHAR24.next());
		return map;
	}

	@Override
	public List<HumaneUserRoleMapVO> findMappingsByUser(String userId) {
		List<HumaneUserRoleMap> mappings = this.userRoleMapRepo.findAllMappingsByUser(userId);
		if (!CollectionUtils.isEmpty(mappings)) {
			return mappings.stream().map(HumaneUserRoleMapVO::new).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<HumaneUserRoleView> findMappingViewOfUser(String userId) {
		return this.userRoleMapRepo.findMappingViewOfUser(userId);
	}

	@Override
	public List<HumaneUserRoleMapVO> findMappingsByRole(String roleId, Integer page, Integer pageSize) {
		List<HumaneUserRoleMap> mappings = this.userRoleMapRepo.findAllMappingsByRole(roleId,
				PageRequest.of(page, pageSize, Direction.ASC, "userRoleId"));
		if (!CollectionUtils.isEmpty(mappings)) {
			return mappings.stream().map(HumaneUserRoleMapVO::new).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public void deleteRoleMapping(String userRoleId) {
		this.userRoleMapRepo.delete(userRoleId);
	}

	@Override
	public void deleteRoleMapping(String userId, String roleId) throws HumaneEntityViolationException {
		Optional<HumaneUserRoleMap> data = this.userRoleMapRepo.findByUserAndRoleId(userId, roleId);
		if (data.isPresent()) {
			this.userRoleMapRepo.delete(data.get().getUserRoleId());
		} else {
			throw new HumaneEntityViolationException(
					"No such mapping found with user [" + userId + "] and role [" + roleId + "]");
		}
	}

}
