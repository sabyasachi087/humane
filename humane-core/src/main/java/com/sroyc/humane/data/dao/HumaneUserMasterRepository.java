package com.sroyc.humane.data.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.sroyc.humane.data.model.Address;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.Phone;

public interface HumaneUserMasterRepository {

	public HumaneUserMaster createUser(HumaneUserMaster hum);

	public HumaneUserMaster updateUser(HumaneUserMaster hum);

	public Optional<HumaneUserMaster> findById(String userId);

	public Optional<HumaneUserMaster> findByEmailOrUsername(String emailOrUsername);

	public List<HumaneUserMaster> findAllWithFilters(HumaneUserMasterFilter filter, Pageable page);

	public HumaneUserMaster toggleActivation(String userId, boolean activation, String modifiedBy);

	public HumaneUserMaster toggleDeletion(String userId, boolean deletion, String modifiedBy);

	public List<Phone> replacePhoneNumbers(String userId, String modifiedBy, List<? extends Phone> phoneNumbers);

	public List<Phone> getPhoneNumbers(String userId);

	public List<Address> replaceAddresses(String userId, String modifiedBy, List<? extends Address> addresses);

	public List<Address> getAddresses(String userId);

}
