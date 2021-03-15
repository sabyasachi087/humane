package com.sroyc.humane.util;

import org.springframework.util.StringUtils;

import com.sroyc.humane.data.model.Address;
import com.sroyc.humane.data.model.HumaneRoleMaster;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.Phone;
import com.sroyc.humane.view.models.AddressVO;
import com.sroyc.humane.view.models.HumaneRoleMasterVO;
import com.sroyc.humane.view.models.HumaneUserMasterVO;
import com.sroyc.humane.view.models.PhoneNumberVO;

public class ValueObjectAdapter {

	private ValueObjectAdapter() {
	}

	public static final HumaneUserMaster transform(HumaneUserMasterVO vo, HumaneUserMaster user) {
		user.setActive(vo.isActive());
		user.setCreatedBy(vo.getCreatedBy());
		user.setCreatedOn(vo.getCreatedOn());
		user.setEmail(vo.getEmail());
		user.setFirstName(vo.getFirstName());
		user.setLastName(vo.getLastName());
		user.setMiddleName(vo.getMiddleName());
		user.setUsername(vo.getUsername());
		user.setUserId(vo.getUserId());
		return user;
	}

	public static final Phone transform(PhoneNumberVO vo, Phone phone) {
		phone.setCountryCode(vo.getCountryCode());
		phone.setPhoneNumber(vo.getPhoneNumber());
		phone.setPrimary(vo.isPrimary());
		return phone;
	}

	public static final Address transform(AddressVO vo, Address address) {
		address.setCity(vo.getCity());
		address.setCodeOrPin(vo.getCodeOrPin());
		address.setCountry(vo.getCountry());
		address.setId(vo.getId());
		address.setLandmark(vo.getLandmark());
		address.setLine1(vo.getLine1());
		address.setLine2(vo.getLine2());
		address.setPrimary(vo.isPrimary());
		address.setState(vo.getState());
		address.setTag(vo.getTag());
		return address;
	}

	public static final void update(HumaneUserMasterVO vo, HumaneUserMaster user) {
		user.setActive(vo.isActive());
		user.setEmail(vo.getEmail());
		user.setFirstName(vo.getFirstName());
		user.setLastName(vo.getLastName());
		user.setMiddleName(vo.getMiddleName());
		if (StringUtils.hasText(vo.getUsername())) {
			user.setUsername(vo.getUsername());
		}else {
			user.setUsername(vo.getEmail());
		}
		user.setDelete(vo.isDelete());
	}

	public static final HumaneRoleMaster transform(HumaneRoleMasterVO vo, HumaneRoleMaster role) {
		role.setDeprecate(vo.isDeprecate());
		role.setRoleCode(vo.getRoleCode());
		role.setRoleId(vo.getRoleId());
		role.setRoleName(vo.getRoleName());
		role.setHierarchy(vo.getHierarchy());
		return role;
	}

}
