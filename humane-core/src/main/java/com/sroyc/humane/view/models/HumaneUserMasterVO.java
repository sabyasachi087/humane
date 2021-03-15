package com.sroyc.humane.view.models;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sroyc.humane.data.model.HumaneUserMaster;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HumaneUserMasterVO extends AuditMarkerVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7440990645282431714L;

	private String userId;
	private String email;
	private String username;
	private String firstName;
	private String lastName;
	private String middleName;
	private boolean active;
	private boolean delete;
	private List<PhoneNumberVO> phones;
	private List<AddressVO> addresses;

	public HumaneUserMasterVO(HumaneUserMaster hum) {
		super(hum);
		this.userId = hum.getUserId();
		this.email = hum.getEmail();
		this.username = hum.getUsername();
		this.firstName = hum.getFirstName();
		this.lastName = hum.getLastName();
		this.middleName = hum.getMiddleName();
		this.active = hum.isActive();
		this.delete = hum.isDelete();
		if (!CollectionUtils.isEmpty(hum.getPhoneNumbers())) {
			this.phones = hum.getPhoneNumbers().stream().map(PhoneNumberVO::new).collect(Collectors.toList());
		}
		if (!CollectionUtils.isEmpty(hum.getAddresses())) {
			this.addresses = hum.getAddresses().stream().map(AddressVO::new).collect(Collectors.toList());
		}

	}

	public HumaneUserMasterVO() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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

	public List<PhoneNumberVO> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneNumberVO> phones) {
		this.phones = phones;
	}

	public List<AddressVO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressVO> addresses) {
		this.addresses = addresses;
	}

}
