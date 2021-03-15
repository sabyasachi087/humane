package com.sroyc.humane.data.model.mongo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.sroyc.humane.data.model.Address;
import com.sroyc.humane.data.model.EntityDataModelFactory;
import com.sroyc.humane.data.model.HumaneConfigurationSetting;
import com.sroyc.humane.data.model.HumaneRoleMaster;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.HumaneUserRoleMap;
import com.sroyc.humane.data.model.Phone;

@Component
@Profile("sroyc.data.mongo")
public class MongoEntityDataFactoryModel implements EntityDataModelFactory {

	@Override
	public Address address() {
		return new MongoAddressData();
	}

	@Override
	public Phone phone() {
		return new MongoPhoneData();
	}

	@Override
	public HumaneRoleMaster role() {
		return new MongoRoleMasterEntity();
	}

	@Override
	public HumaneUserMaster user() {
		return new MongoUserMasterEntity();
	}

	@Override
	public HumaneUserRoleMap userRole() {
		return new MongoUserRoleMapEntity();
	}

	@Override
	public HumaneConfigurationSetting configSetting() {
		return new MongoConfigurationSettingEntity();
	}

}
