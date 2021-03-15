package com.sroyc.humane.data.model.mongo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import com.sroyc.humane.data.model.Address;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.Phone;

@Profile("sroyc.data.mongo")
@Document(collection = "user_master")
public class MongoUserMasterEntity implements HumaneUserMaster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 525078785444158359L;

	@Id
	private String userId;
	@Indexed(name = "usr_mst_username_uk", unique = true)
	private String username;
	@Indexed(name = "usr_mst_email_uk", unique = true)
	private String email;
	private String firstName;
	private String middleName;
	private String lastName;
	@Indexed(name = "usr_mst_fullname_indx")
	private String fullname;
	private String secret;
	private boolean active;
	private boolean delete;
	private List<MongoPhoneData> phoneNumbers;
	private List<MongoAddressData> addresses;
	private String modifiedBy;
	private LocalDateTime modifiedOn;
	private String createdBy;
	private LocalDateTime createdOn;
	@Version
	private Integer version;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public List<Phone> getPhoneNumbers() {
		if (!CollectionUtils.isEmpty(this.phoneNumbers)) {
			return this.phoneNumbers.stream().map(pn -> (Phone) pn).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	public void setPhoneNumbers(List<? extends Phone> phoneNumbers) {
		if (!CollectionUtils.isEmpty(phoneNumbers)) {
			this.phoneNumbers = new ArrayList<>();
			this.phoneNumbers.addAll((Collection<MongoPhoneData>) phoneNumbers);
		} else {
			this.phoneNumbers = new ArrayList<>();
		}
	}

	public List<Address> getAddresses() {
		if (!CollectionUtils.isEmpty(this.addresses)) {
			return this.addresses.stream().map(ad -> (Address) ad).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setAddresses(List<? extends Address> addresses) {
		if (!CollectionUtils.isEmpty(addresses)) {
			this.addresses = new ArrayList<>();
			this.addresses.addAll((Collection<MongoAddressData>) addresses);
		} else {
			this.addresses = new ArrayList<>();
		}
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
