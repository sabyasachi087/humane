package com.sroyc.humane.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sroyc.humane.data.dao.HumaneConfigurationSettingRepository;
import com.sroyc.humane.data.dao.HumaneRoleMasterRespository;
import com.sroyc.humane.data.dao.HumaneUserMasterRepository;
import com.sroyc.humane.data.dao.HumaneUserRoleMapRepository;
import com.sroyc.humane.data.model.EntityDataModelFactory;
import com.sroyc.humane.data.model.HumaneConfigurationSetting;
import com.sroyc.humane.data.model.HumaneRoleMaster;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.HumaneUserRoleMap;
import com.sroyc.humane.exception.HumaneEntityViolationException;
import com.sroyc.humane.util.CommonUtils;
import com.sroyc.humane.util.UniqueSequenceGenerator;
import com.sroyc.humane.view.models.HumaneRoleMasterVO;
import com.sroyc.humane.view.models.HumaneUserDetails;
import com.sroyc.humane.view.models.HumaneUserMasterVO;

public class HumaneUserDetailsServiceImpl implements HumaneUserDetailsService {

	@Autowired
	private HumaneRoleMasterRespository roleRepo;
	@Autowired
	private HumaneUserMasterRepository userRepo;
	@Autowired
	private HumaneUserRoleMapRepository userRoleRepo;
	@Autowired
	private HumaneConfigurationSettingRepository configRepo;
	@Autowired
	private EntityDataModelFactory entityModelFactory;

	@Override
	public HumaneUserDetails loadUser(String emailOrUsername) {
		Optional<HumaneUserMaster> entity = this.userRepo.findByEmailOrUsername(emailOrUsername);
		if (entity.isPresent()) {
			HumaneUserDetails hud = new HumaneUserDetails();
			hud.setUser(new HumaneUserMasterVO(entity.get()));
			hud.setSecret(entity.get().getSecret());
			List<HumaneUserRoleMap> mappings = this.userRoleRepo.findAllMappingsByUser(entity.get().getUserId());
			List<HumaneRoleMasterVO> roles = new ArrayList<>();
			if (!CollectionUtils.isEmpty(mappings)) {
				mappings.forEach(hurm -> {
					Optional<HumaneRoleMaster> role = this.roleRepo.findById(hurm.getRoleId());
					if (role.isPresent()) {
						roles.add(new HumaneRoleMasterVO(role.get()));
					}
				});
			}
			hud.setRoles(roles);
			return hud;
		}
		return null;
	}

	@Override
	public HumaneUserDetails loadUser(Map<String, String> attributes) throws HumaneEntityViolationException {
		Map<HumaneConfigurationSetting.Attribute, String> userAttributes = this.configRepo.getUserAttributes();
		Map<HumaneConfigurationSetting.SettingKey, String> setting = this.configRepo.getHumaneSettings();
		String email = attributes.get(userAttributes.get(HumaneConfigurationSetting.Attribute.EMAIL));
		String username = attributes.get(userAttributes.get(HumaneConfigurationSetting.Attribute.USERNAME));
		HumaneUserDetails hud = null;
		if (StringUtils.hasText(email)) {
			hud = this.loadUser(email);
		}
		if (hud == null && StringUtils.hasText(username)) {
			hud = this.loadUser(username);
		}
		if (hud == null && !CollectionUtils.isEmpty(setting)
				&& Boolean.parseBoolean(setting.get(HumaneConfigurationSetting.SettingKey.AUTO_CREATE_USER))) {
			HumaneUserMaster hum = this.createUserInstance(userAttributes, attributes);
			hud = new HumaneUserDetails();
			hud.setUser(new HumaneUserMasterVO(hum));
			hud.setSecret(null);
			this.defineRole(hud, setting);
		}
		return hud;
	}

	protected HumaneUserMaster createUserInstance(Map<HumaneConfigurationSetting.Attribute, String> setting,
			Map<String, String> attributes) throws HumaneEntityViolationException {
		if (!CollectionUtils.isEmpty(setting)) {
			String username = attributes.get(setting.get(HumaneConfigurationSetting.Attribute.USERNAME));
			String email = attributes.get(setting.get(HumaneConfigurationSetting.Attribute.EMAIL));
			String firstName = attributes.get(setting.get(HumaneConfigurationSetting.Attribute.FIRST_NAME));
			String lastName = attributes.get(setting.get(HumaneConfigurationSetting.Attribute.LAST_NAME));
			String fullName = CommonUtils.fullname(firstName, lastName, null);
			username = StringUtils.hasText(username) ? username : email;
			if (!StringUtils.hasText(email)) {
				throw new HumaneEntityViolationException("Unable to create user as email is empty");
			}
			HumaneUserMaster hum = this.entityModelFactory.user();
			hum.setUserId(UniqueSequenceGenerator.CHAR16.next());
			hum.setActive(true);
			hum.setCreatedBy(username);
			hum.setFirstName(firstName);
			hum.setLastName(lastName);
			hum.setFullname(fullName);
			hum.setDelete(false);
			hum.setEmail(email);
			hum.setUsername(username);
			return this.userRepo.createUser(hum);
		}
		return null;
	}

	protected void defineRole(HumaneUserDetails hud, Map<HumaneConfigurationSetting.SettingKey, String> setting) {
		if (!CollectionUtils.isEmpty(setting)
				&& StringUtils.hasText(setting.get(HumaneConfigurationSetting.SettingKey.DEFAULT_ROLE))) {
			String roleCode = setting.get(HumaneConfigurationSetting.SettingKey.DEFAULT_ROLE);
			Optional<HumaneRoleMaster> role = this.roleRepo.findByRoleCode(roleCode);
			if (role.isPresent()) {
				this.mapRole(hud.getUser().getUserId(), role.get().getRoleId(), hud.getUser().getEmail());
				List<HumaneRoleMasterVO> roles = new ArrayList<>();
				roles.add(new HumaneRoleMasterVO(role.get()));
				hud.setRoles(Collections.unmodifiableList(roles));
			}
		}
	}

	protected void mapRole(String userId, String roleId, String email) {
		HumaneUserRoleMap map = this.entityModelFactory.userRole();
		map.setCreatedBy(email);
		map.setCreatedOn(LocalDateTime.now());
		map.setRoleId(roleId);
		map.setUserId(userId);
		map.setUserRoleId(UniqueSequenceGenerator.CHAR24.next());
		this.userRoleRepo.create(map);
	}

}
