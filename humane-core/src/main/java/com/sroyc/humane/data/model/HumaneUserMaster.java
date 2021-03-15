package com.sroyc.humane.data.model;

import java.util.List;

public interface HumaneUserMaster extends AuditMarker {

	public String getUserId();

	public void setUserId(String userId);

	public String getUsername();

	public void setUsername(String username);

	public String getEmail();

	public void setEmail(String email);

	public String getFirstName();

	public void setFirstName(String firstName);

	public String getMiddleName();

	public void setMiddleName(String middleName);

	public String getLastName();

	public void setLastName(String lastName);

	public String getFullname();

	public void setFullname(String fullname);

	public String getSecret();

	public void setSecret(String secret);

	public boolean isActive();

	public void setActive(boolean active);

	public boolean isDelete();

	public void setDelete(boolean delete);

	public List<Phone> getPhoneNumbers();

	public void setPhoneNumbers(List<? extends Phone> phoneNumbers);

	public List<Address> getAddresses();

	public void setAddresses(List<? extends Address> addresses);

}
