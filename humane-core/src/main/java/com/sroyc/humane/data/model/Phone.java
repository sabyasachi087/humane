package com.sroyc.humane.data.model;

import java.io.Serializable;

public interface Phone extends Serializable {

	public String getCountryCode();

	public void setCountryCode(String countryCode);

	public String getPhoneNumber();

	public void setPhoneNumber(String phoneNumber);

	public boolean isPrimary();

	public void setPrimary(boolean primary);

}
